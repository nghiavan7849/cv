import { StyleSheet, Text, TouchableOpacity, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import className from 'twrnc';
import MenuTop from "./menu/MenuTop";


const Chat = ({navigation}) => {
    return (
        <SafeAreaView  style={styles.container}>
            {/* <MenuTop title="Chat"></MenuTop> */}
            {/* <View style={className`mt-2 items-center`}><Text style={className`text-2xl`}>Chat</Text></View> */}
            <View style={className`w-[100%] mt-5 justify-center items-center`}>
                <TouchableOpacity onPress={()=>navigation.navigate("ChatBot")} style={className`w-[100%] mb-3 py-3 justify-center items-center border-2 rounded-lg`}>
                    <Text style={className`text-lg`}>Chat AI</Text>
                </TouchableOpacity>
            </View>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16
    }
});

export default Chat;