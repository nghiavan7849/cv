import React from "react";
import { Button, Text, TouchableOpacity, View, Platform, SafeAreaView } from "react-native";
import { StyleSheet } from "react-native";
import className from 'twrnc';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from "@react-navigation/native";


const MenuTop = ({ title = '', navi = '' }) => {

    const naviagtion = useNavigation();

    return (
        <SafeAreaView>
            <View style={[styles.container, className`flex flex-row `]} >
                <TouchableOpacity style={className`basis-1/9 pl-2`} onPress={()=> navi? naviagtion.reset({index:0,routes:[{name: navi}]}):naviagtion.goBack()}>
                    <Ionicons name="arrow-back" size={28} color="black" />
                </TouchableOpacity>
                <View style={className`basis-7/9`}>
                    {title ? <Text style={styles.title}>{title}</Text> : <></>}
                </View>
                <View style={className`basis-1/9`}></View>
            </View>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        height: Platform.OS === 'android' ? 60 : 30,
        // marginTop: Platform.OS === 'android' ?32:0,
        // height:60,
        // marginTop:32,
        // backgroundColor: '#FFFFFF',
        backgroundColor:'#F5F0DC',
        justifyContent: 'center',
        alignItems: 'center',
        
    },
    title: {
        color: '#000',
        textAlign:'center',
        fontWeight: 'bold',
        fontSize: 24,
    }

});

export default MenuTop;