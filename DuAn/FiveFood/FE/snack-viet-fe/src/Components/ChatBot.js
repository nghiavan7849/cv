import React, { useEffect, useState, useContext, useRef } from "react";
import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";
import { motion } from "framer-motion";
import {
  Avatar,
  ChatContainer,
  Message,
  MessageInput,
  MessageList,
  Sidebar,
  TypingIndicator,
} from "@chatscope/chat-ui-kit-react";
import { GoogleGenerativeAI } from "@google/generative-ai";
import { ConversationList, Conversation } from "@chatscope/chat-ui-kit-react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { addDoc, collection, doc, getDocs, orderBy, query, setDoc, Timestamp } from "firebase/firestore";
import { db } from "./Firebase";
import { v4 } from "uuid";
import { AuthContext } from "../Service/AuthContext";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const urlApi = process.env.REACT_APP_API_URL;

const adminAvatar = require("../images/software-engineer.png");
const genAI = new GoogleGenerativeAI(process.env.REACT_APP_API_KEY);
const model = genAI.getGenerativeModel({
  model: 'tunedModels/fivefoodchatbotmodelnew-ggnlz4us0srt',
  generationConfig: {
    temperature: 0.3,
    maxOutputTokens: 50
  }
});

const ChatBot = () => {
  const navigate = useNavigate();
  const { taiKhoan } = useContext(AuthContext);
  const [currentChat, setCurrentChat] = useState("Chatbot");
  const [botMessages, setBotMessages] = useState([]);
  const [adminMessages, setAdminMessages] = useState([]);
  const [userMessages, setUserMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [isTyping, setIsTyping] = useState(false);
  const [isOpen, setIsOpen] = useState(false); // Trạng thái mở/thu nhỏ bubble
  const [stompClient, setStompClient] = useState(null);
  const [connected, setConnected] = useState(false);

  const messagesEndRef = useRef(null); // Dùng để cuộn xuống cuối khi có tin nhắn mới

useEffect(() => {
  // Khi có tin nhắn mới, cuộn xuống cuối
  if (messagesEndRef.current) {
    messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
  }
}, [adminMessages, userMessages, botMessages]);

  // Lấy thông tin tài khoản từ sessionStorage
  useEffect(() => {
    const sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    // setTaiKhoan(sessionTaiKhoan);
    handleChatSelection(currentChat);
  }, []);

  const loadChatMessages = async () => {
    try {
      let chatId;
      
      if (taiKhoan?.vaiTro) { // Admin
        chatId = `admin_${taiKhoan.maTaiKhoan}`;
      } else { // User
        chatId = `user_${taiKhoan.maTaiKhoan}`;
      }
  
      // Truy vấn các tin nhắn từ Firestore
      const messagesRef = collection(db, "chats", chatId, "messages");
      const q = query(messagesRef, orderBy("timestamp", "asc"));
      const querySnapshot = await getDocs(q);
      
      const messages = querySnapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));
      
      return messages;
    } catch (error) {
      console.error("Lỗi khi tải tin nhắn từ Firestore:", error);
      return [];
    }
  };
  
  // Gọi hàm để lấy tin nhắn khi tải lại trang
  useEffect(() => {
    const fetchMessages = async () => {
      const messages = await loadChatMessages();
      if (taiKhoan?.vaiTro) {
        setAdminMessages(messages);
      } else {
        setUserMessages(messages);
      }
    };
  
    fetchMessages();
  }, [taiKhoan?.maTaiKhoan]); // Khi maTaiKhoan thay đổi thì load lại tin nhắn

  useEffect(() => {
    if (taiKhoan?.vaiTro) {
      // Nếu là admin
      setCurrentChat("Admin");
    } else {
      // Nếu là người dùng
      setCurrentChat("Chatbot");
    }
  }, [taiKhoan]);

  // Kết nối WebSocket
  useEffect(() => {

    const socket = new SockJS(`${urlApi}/ws`);
    const client = Stomp.over(socket);

    client.connect(
      {},
      () => {
        setConnected(true);

        if (taiKhoan?.vaiTro) {
          // Admin nhận tin nhắn từ người dùng
          client.subscribe("/topic/admins", async(message) => {
            const receivedMessage = JSON.parse(message.body);
            const incomingMessage = {
              id: Date.now(),
              sender: receivedMessage.nickname,
              text: receivedMessage.content,
              timestamp: new Date().toLocaleTimeString(),
              direction: "incoming",
            };
            // Lưu tin nhắn vào Firestore
            const chatId = `admin_${taiKhoan.maTaiKhoan}`;
            await addChatMessage(chatId, incomingMessage);
            setAdminMessages((prevMessages) => [...prevMessages, incomingMessage]);
          });
        } else {
          // Người dùng nhận phản hồi từ admin qua kênh cá nhân
          client.subscribe(`/topic/${taiKhoan?.maTaiKhoan}`, async(message) => {
            const receivedMessage = JSON.parse(message.body);
            const incomingMessage = {
              id: Date.now(),
              sender: "Admin",
              text: receivedMessage.content,
              timestamp: new Date().toLocaleTimeString(),
              direction: "incoming",
            };
            // Lưu tin nhắn vào Firestore
          const chatId = `user_${taiKhoan.maTaiKhoan}`;
          await addChatMessage(chatId, incomingMessage);
            setUserMessages((prevMessages) => [...prevMessages, incomingMessage]);
          });
        }
      },
      (error) => console.error("Lỗi kết nối WebSocket:", error)
    );

    setStompClient(client);

    return () => {
      if (client && client.connected) {
        client.disconnect();
      }
    };
  }, [taiKhoan?.maTaiKhoan, taiKhoan?.vaiTro]);

  // Tách response và intent
const parseResponse = async (generatedResponse) => {
  try {
    const prompt = `
      Analyze the following response and extract two components:
      1. "Response" - The main content or answer.
      2. "Intent" - The identified intent for the given query.

      Response: "${generatedResponse}"

      Ensure you return a single, concise JSON object in the format:
      {
        "response": "Main content here",
        "intent": "Intent here"
      }
      Do not include any additional text or repetitions.
    `;

    const result = await model.generateContent(prompt);
    const responseText = await result.response?.text();

    console.log(responseText);

    // Tìm JSON đầu tiên xuất hiện trong phản hồi
    const match = responseText.match(/\{.*?\}/s);
    if (match) {
      return JSON.parse(match[0]);
    } else {
      throw new Error("Invalid response format");
    }
  } catch (error) {
    console.error('Error parsing response:', error);
    return { response: null, intent: null };
  }
};

  // Function to extract the product name from the user's input (e.g., "Cơm gà")
const extractProductName = async(userInput) => {
  const productKeywords = await axios.get(`${urlApi}/api/chat-bot/productNames`);  // Add more keywords as needed
  console.log(userInput.toLowerCase());
  for (const keyword of productKeywords.data.data) {
    if (userInput.toLowerCase().includes(keyword.toLowerCase())) {
        return keyword;  // Return the product name found in the user input
    }
  }
  return 'unknown';  // Return 'unknown' if no product is found
};

  const handleNavigate = (productLink)=>{
    navigate(productLink);
  }

  const getBotResponse = async (input) => {
    setIsTyping(true);
    try {
      const result = await model.generateContent(input);
      let responseMessage = result.response?.text() || "Không thể tạo phản hồi!";

      // Gọi hàm tách giữa response và intent
      const { response: botResponse, intent } = await parseResponse(responseMessage);

      if (intent === 'DYNAMIC' && (botResponse === 'CHECK_PRODUCT_AVAILABILITY' || botResponse === 'GET_PRODUCT_AVAILABILITY')) {
        const extractedVariable = await extractProductName(input);
        console.log("Đây là variable: ",extractedVariable);
        if(extractedVariable === 'unknown'){
          return `Chúng tôi không có sản phẩm mà bạn đã yêu cầu`;      
        }
        const callApi = await axios.get(`${urlApi}/api/chat-bot/${botResponse}?keyword=${extractedVariable}`);
        // Tạo đường dẫn với sản phẩm
        const productLink = `/home/product-details/${callApi.data.data}`;
      
        // Trả về tin nhắn có đường dẫn
        return `Bạn đã đặt câu hỏi về <a onClick=${handleNavigate(productLink)} style="color: blue; text-decoration: underline;">${extractedVariable}</a>`;      
      } else if(intent === 'DYNAMIC' && botResponse === 'GET_TOP_PRODUCT'){
        const callApi = await axios.get(`${urlApi}/api/chat-bot/${botResponse}`);
        console.log(callApi.data.data);
        // Tạo đường dẫn với sản phẩm
        const productLink = `/home/product-details/${callApi.data.data[0]}`;
      
        // Trả về tin nhắn có đường dẫn
        return `Sản phẩm bán chạy nhất hiện tại là <a onClick=${handleNavigate(productLink)} style="color: blue; text-decoration: underline;">${callApi.data.data[1]}</a> với tổng ${callApi.data.data[2]} lượt bán`;
      }
      return botResponse;
    } catch (error) {
      console.error("Lỗi khi gửi tin nhắn:", error);
      return "ChatBot gặp sự cố!";
    } finally {
      setIsTyping(false);
    }
  };

  // Hàm thêm tin nhắn vào Firestore
  const addChatMessage = async (chatId, message) => {
    try {
      // Đảm bảo document cha tồn tại với metadata
      const chatRef = doc(db, "chats", chatId);
      await setDoc(chatRef, { createdAt: Timestamp.now() }, { merge: true });
  
      // Xác thực và chuẩn hóa dữ liệu tin nhắn
      const messageData = {
        text: message.text || "",
        sender: message.sender || "unknown",
        timestamp: Timestamp.now(),
        direction: message.direction || "outgoing",
      };
  
      // Lưu tin nhắn vào subcollection "messages"
      await addDoc(collection(db, "chats", chatId, "messages"), messageData);
      console.log("Tin nhắn đã được lưu vào Firestore!", messageData);
    } catch (error) {
      console.error("Lỗi khi lưu tin nhắn vào Firestore:", error);
    }
  };

  const handleSendMessage = async () => {
    if (newMessage.trim() === "") return;
  
    const message = {
      id:null,
      sender: null,
      timestamp:null,
      direction:"",
    }

     // Lưu tin nhắn vào Firestore
    let chatId;
  
    // Nếu đang trò chuyện với ChatBot
    if (currentChat === "Chatbot") {

      const message = {
        id: Date.now(), 
        sender: taiKhoan?.maTaiKhoan || "Người dùng",
        text: newMessage,
        timestamp: new Date().toLocaleTimeString(),
        direction: "outgoing",
      };
      setBotMessages((prevMessages) => [...prevMessages, message]);
      setNewMessage("");

      const botResponse = await getBotResponse(newMessage);
      const botMessage = {
        id: Date.now(),
        sender: "Chat Bot",
        text: botResponse,
        timestamp: new Date().toLocaleTimeString(),
        direction: "incoming",
      };
      // Cập nhật phản hồi từ ChatBot vào danh sách tin nhắn
      setBotMessages((prevMessages) => [...prevMessages, botMessage]);
    } else if (taiKhoan?.vaiTro) {
      const message = {
        id: Date.now(), 
        sender: taiKhoan?.maTaiKhoan || "Admin",
        text: newMessage,
        timestamp: new Date().toLocaleTimeString(),
        direction: "outgoing",
      };
      chatId = `admin_${taiKhoan.maTaiKhoan}`; // Định danh cho cuộc trò chuyện với admin
      await addChatMessage(chatId,message)
      setAdminMessages((prevMessages) => [...prevMessages, message]);
      setNewMessage("");

      // Nếu là admin gửi tin nhắn, gửi tới WebSocket
      stompClient.send("/app/reply", {}, JSON.stringify({
        id: Date.now(),
        nickname: taiKhoan?.maTaiKhoan || "Người dùng",
        content: newMessage,
      }));
    } else {
      const message = {
        id: Date.now(), 
        sender: taiKhoan?.maTaiKhoan || "Người dùng",
        text: newMessage,
        timestamp: new Date().toLocaleTimeString(),
        direction: "outgoing",
      };
      chatId = `user_${taiKhoan.maTaiKhoan}`;
      await addChatMessage(chatId, message);
      setUserMessages((prevMessages) => [...prevMessages, message]);
      
      setNewMessage("");

      // Người dùng gửi tin nhắn tới admin qua WebSocket
      stompClient.send("/app/chat", {}, JSON.stringify({
        id: Date.now(),
        nickname: taiKhoan?.maTaiKhoan || "Người dùng",
        content: newMessage,
      }));
    }
  };

  const handleChatSelection = (chatOption) => {
    setCurrentChat(chatOption);

    if (chatOption === "Chatbot") {
      // Tập hợp các tin nhắn chào mừng cho Chat Bot
      const botWelcomeMessages = [
        {
          id: Date.now(),
          sender: "Chat Bot",
          text: "Chào bạn! Tôi là Chat Bot. Hãy cho tôi biết câu hỏi của bạn.",
          timestamp: new Date().toLocaleTimeString(),
          direction: "incoming",
        },
        {
          id: Date.now() + 1, // Đảm bảo id duy nhất
          sender: "Function",
          type:'function',
          text: "Sản phẩm bán chạy nhất hiện nay là gì ?",
          timestamp: new Date().toLocaleTimeString(),
          direction: "incoming",
        },
        {
          id: Date.now() + 2,
          sender: "Function",
          type:'function',
          text: "Hướng dẫn coi lịch sử đơn hàng",
          timestamp: new Date().toLocaleTimeString(),
          direction: "incoming",
        },
      ];
  
      setBotMessages((prevMessages) => {
        // Lọc ra các tin nhắn chưa có trong danh sách tin nhắn hiện tại
        const newMessages = botWelcomeMessages.filter(
          (newMessage) =>
            !prevMessages.some(
              (message) =>
                message.text === newMessage.text && message.sender === newMessage.sender
            )
        );
        return [...prevMessages, ...newMessages];
      });
    }
    else if(chatOption==='User'){
      const adminWelcomeMessage = {
        id: Date.now(),
        sender: "Admin",
        text: "Chào bạn! Nhắn với admin nếu bạn khó khăn gì nhé!",
        timestamp: new Date().toLocaleTimeString(),
        direction: "incoming"
      }
      setUserMessages((prevMessages) => {
        // Kiểm tra nếu đã có tin nhắn chào mừng
        const hasWelcomeMessage = prevMessages.some(
          (message) => message.text === adminWelcomeMessage.text && message.sender === adminWelcomeMessage.sender
        );
        // Chỉ thêm tin nhắn nếu chưa có
        return hasWelcomeMessage ? prevMessages : [...prevMessages, adminWelcomeMessage];
      });
    }
    
  };

  const handleQuickReply = async(quickReplyMessage) => {
    console.log(quickReplyMessage)
    const newMessage = {
      id: Date.now(), 
      sender: taiKhoan?.maTaiKhoan || "Người dùng",
      text: quickReplyMessage,
      timestamp: new Date().toLocaleTimeString(),
      direction: "outgoing",
    };
    setBotMessages((prevMessages) => [...prevMessages, newMessage]);

    // Phản hồi từ bot
    const botResponse = await getBotResponse(quickReplyMessage);
    console.log(botResponse)
    const botMessage = {
      id: Date.now()+1,
      sender: "Chat Bot",
      text: botResponse,
      timestamp: new Date().toLocaleTimeString(),
      direction: "incoming",
    };
    setBotMessages((prevMessages) => [...prevMessages, botMessage]);
  };

  return (
    <motion.div
  style={{
    position: "fixed",
    bottom: "20px",
    right: "20px",
    zIndex: 1000,
  }}
  initial={{ width: "60px", height: "60px", borderRadius: "50%" }}
  animate={{
    width: isOpen ? "600px" : "60px",
    height: isOpen ? "500px" : "60px",
    borderRadius: isOpen ? "16px" : "50%",
  }}
  transition={{ type: "spring", stiffness: 300, damping: 30 }}
>
  <div
    style={{
      width: "100%",
      height: "100%",
      backgroundColor: isOpen ? "#fff" : "#067A38",
      overflow: "hidden",
      boxShadow: "0 4px 8px rgba(0,0,0,0.2)",
      position: "relative",
    }}
    onClick={() => setIsOpen(true)}
  >
    {!isOpen && (
      <motion.div
        style={{
          width: "100%",
          height: "100%",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          color: "#fff",
          fontSize: "24px",
          cursor: "pointer",
        }}
      >
        💬
      </motion.div>
    )}
    {isOpen && (
      <div style={{ height: "100%", display: "flex", flexDirection: "column" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            backgroundColor: "#067A38",
            color: "#fff",
            padding: "8px 12px",
            cursor: "pointer",
          }}
        >
          <span>Chat Hỗ Trợ</span>
          <button
            style={{
              backgroundColor: "transparent",
              border: "none",
              color: "#fff",
              cursor: "pointer",
              fontSize: "16px",
            }}
            onClick={(e) => {
              e.stopPropagation();
              setIsOpen(false);
            }}
          >
            ➖
          </button>
        </div>
        <div style={{ flex: 1, display: "flex" }}>
          <Sidebar style={{ width: "200px", borderRight: "1px solid #ddd" }}>
            <ConversationList>
              <Conversation
                name="Chat Bot"
                info="Hỗ trợ AI"
                onClick={() => handleChatSelection("Chatbot")}
              >
                <Avatar src="https://www.svgrepo.com/show/353774/geekbot.svg" name="Chat Bot" />
              </Conversation>
              {taiKhoan ? (
                taiKhoan.vaiTro ? (
                  <Conversation
                    name="Người dùng"
                    info="Tin nhắn từ người dùng"
                    onClick={() => handleChatSelection("Admin")}
                  >
                    <Avatar src={adminAvatar} name="Người dùng" />
                  </Conversation>
                ) : (
                  <Conversation
                    name="Admin"
                    info="Tin nhắn từ admin"
                    onClick={() => handleChatSelection("User")}
                  >
                    <Avatar src={adminAvatar} name="Người dùng" />
                  </Conversation>
                )
              ) : null}
            </ConversationList>
          </Sidebar>
          <ChatContainer style={{ width: "400px", overflowY: "auto", maxHeight: "460px" }}>
            <MessageList typingIndicator={isTyping && currentChat === "Chatbot" && <TypingIndicator content="Chatbot đang gõ..." />}>
              {(currentChat === "Chatbot" ? botMessages : (taiKhoan?.vaiTro ? adminMessages : userMessages)).map((msg) => (
                <Message
                  onClick={msg.type === 'function' ? () => handleQuickReply(msg.text) : null}
                  key={msg.id}
                  style={msg.type === 'function' 
                    ? { 
                        cursor: 'pointer', 
                        backgroundColor: '#f0f8ff',  // Màu nền nhẹ để dễ nhận diện
                        border: '1px solid #007bff',  // Viền màu xanh để làm nổi bật
                        borderRadius: '10px',  // Góc bo tròn
                        padding: '5px 10px',  // Thêm khoảng cách bên trong
                        fontWeight: 'bold'  // In đậm để dễ nhận diện
                    } 
                    : {}
                  }
                  model={{
                    message: msg.text,
                    sentTime: msg.timestamp,
                    sender: msg.sender,
                    direction: msg.direction,
                    position: "normal",
                  }}
                >
                  <Avatar
                    src={
                      msg.sender === "Chat Bot"
                        ? "https://www.svgrepo.com/show/353774/geekbot.svg"
                        : msg.sender === "Admin"
                        ? adminAvatar
                        : msg.sender === "Function"
                        ? "https://www.svgrepo.com/show/188245/pointing-right-finger.svg"
                        : "https://www.svgrepo.com/show/341256/user-avatar-filled.svg"
                    }
                    name={msg.sender}
                  />
                </Message>
              ))}
            </MessageList>
            <MessageInput
              placeholder="Nhập tin nhắn của bạn..."
              value={newMessage}
              onChange={(value) => setNewMessage(value)}
              onSend={handleSendMessage}
              attachButton={false}
            />
          </ChatContainer>
        </div>
      </div>
    )}
  </div>
</motion.div>
  );
};

export default ChatBot;