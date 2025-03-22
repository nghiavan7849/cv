import { StyleSheet, Text, View } from 'react-native';
import React, { useEffect, useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import MenuTop from './menu/MenuTop';
import className from 'twrnc';
import { GiftedChat, IMessage } from 'react-native-gifted-chat';
import axios from 'axios';
import { flaskUrl } from '../service/ApiUrl';
import Toast from 'react-native-toast-message';

const ChatBot = () => {
    const botAvatar = require('../assets/images/technical-support.png');

    const bot = {
        id: 2,
        name: "Mr. Bot",
        avatar: botAvatar
    };

    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setMessages([
            {
                _id: 2,
                text: 'Xin chào! Tôi có thể giúp gì cho bạn?',
                type: 'text',
                createdAt: new Date(),
                user: {
                    _id: 2,
                    name: 'Bot',
                    avatar: botAvatar
                },
            },
            {
                _id: 1,
                text: 'Sản phẩm bán chạy nhất hiện tại là gì ?',
                type: 'function',
                createdAt: new Date(),
                user: {
                    _id: 2,
                    name: 'Bot',
                    avatar: botAvatar
                },
            }
        ]);
    }, []);

    const onSend = (newMessages = []) => {
        setMessages((previousMessages) =>
            GiftedChat.append(previousMessages, newMessages)
        );

        const message = newMessages[0].text;

        handleChatResponse(message);
    };

    const handleChatResponse = async (message) => {
        const data = {
            message: message
        };

        try {
            setLoading(true);
            const response = await axios.post(`${flaskUrl}/chatbot`, data, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            setLoading(false);
            console.log("Results: ", response.data.data.text);
            sendBotResponse(response.data.data.text);
        } catch (error) {
            console.log(error);
        }
    };

    const sendBotResponse = (text) => {
        const botMessage = {
            _id: Math.random(),
            text,
            createdAt: new Date(),
            user: {
                _id: 2,
                name: 'Bot',
                avatar: botAvatar
            },
        };
        setMessages((previousMessages) =>
            GiftedChat.append(previousMessages, [botMessage])
        );
    };

    const handlePressMessage = (message) => {
        if (message.type === 'function') {
            Toast.show({
                type: 'info',
                text1: 'Thông báo',
                text2: `Bạn vừa nhấn vào một tin nhắn có type là: ${message.type}`,
            });
        }
    };

    return (
        <SafeAreaView style={styles.container}>
            <MenuTop title="Chat Bot"></MenuTop>
            <GiftedChat
                placeholder="Nhập tin nhắn của bạn..."
                isTyping={loading}
                messages={messages}
                onSend={onSend}
                user={{
                    _id: 1, // ID của người dùng
                }}
                onPress={(context, message) => handlePressMessage(message)} // Xử lý sự kiện nhấn vào tin nhắn
            />
            <Toast />
        </SafeAreaView>
    );
};

export default ChatBot;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16
    }
});
