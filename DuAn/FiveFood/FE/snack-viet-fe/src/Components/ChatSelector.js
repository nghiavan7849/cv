import { ConversationList, Conversation, Avatar } from "@chatscope/chat-ui-kit-react";

const ChatSelector = ({ onSelect }) => {
    return (
        <ConversationList>
            <Conversation
                name="Chat Bot"
                info="Hỗ trợ AI tự động"
                onClick={() => onSelect("bot")}
            >
                <Avatar src="bot-avatar-url" name="Chat Bot" />
            </Conversation>
            <Conversation
                name="Admin"
                info="Trò chuyện trực tiếp với nhân viên hỗ trợ"
                onClick={() => onSelect("admin")}
            >
                <Avatar src="admin-avatar-url" name="Admin" />
            </Conversation>
        </ConversationList>
    );
};

export default ChatSelector
