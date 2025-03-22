import { StyleSheet, Text, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import className from 'twrnc';

const News = () => {
    return (
        <SafeAreaView  style={styles.container}>
           <View style={className`justify-center items-center`}>
            <Text style={className`text-lg`}>Tin tức</Text>
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

export default News;