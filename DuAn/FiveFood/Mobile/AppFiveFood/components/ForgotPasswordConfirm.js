import {
  Image,
    ScrollView,
    StatusBar,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    View,
  } from "react-native";
  import React, { useRef, useState } from "react";
  import className from "twrnc";
  import { Feather } from "@expo/vector-icons";
  import MenuTop from "./menu/MenuTop";
  import { MaHoaMK } from "../service/MaHoaMK";
  import axios from "axios";
  import Toast from "react-native-toast-message";
  import { Ionicons } from '@expo/vector-icons';
  import  {ApiUrl}  from '../service/ApiUrl';
  import { SafeAreaView } from "react-native-safe-area-context";

const ForgotPasswordConfirm = ({route,navigation}) => {

    // Lấy dữ liệu từ route.params
    // const { email, codeOTP } = route.params;
    const [checkXemMK,isCheckXemMK] = useState(true);
    const inputRefTDN = useRef(null);
    const inputRefMK = useRef(null);
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");
    const [confirmPasswordMessage, setConfirmPasswordMessage] = useState("");

    const postForgotPasswordChange = (data) => {
      return axios.post(`${ApiUrl}/api/tai-khoan/forgot-password/change`,data);
    }

    const clickXacNhan = async () => {
      if(checkValidate()){
          const data = {
              "email":email,
              "matKhau":password,
              "maXacNhan":codeOTP  
          }
          let res = await postForgotPasswordChange(data);
          if(res.data.status === 'success'){
              Toast.show({
                type:'success',
                text1:res.data.message
              })
              setTimeout(() => {
                  navigation.navigate("Login", {replace:true});
              }, 3000);
          }
      }
    };

    const checkValidate = () => {
      if (!password) {
          setPasswordMessage('Mật khẩu mới không được để trống.');
          return false;
      }
      if (!confirmPassword) {
          setConfirmPasswordMessage('Xác nhận mật khẩu mới không được để trống.');
          return false;
      }
      if(confirmPassword !== password ){
          setConfirmPasswordMessage("Xác nhận mật khẩu không khớp");
          return false;
      }

      return true;
    }

    return (
      
        <SafeAreaView style={[styles.container,className`bg-[#F5F0DC]`]}>
          <MenuTop title='Xác nhận mật khẩu' />
          
          <ScrollView
            contentContainerStyle={className`justify-center items-center`}
          >
            <View style={className`items-center`}>
                <Image source={require('../assets/LogoFiveFoodXoaNen.png')} style={className`w-70 h-70`} />
              </View>
            <View style={className`w-full px-5`}>
           
              
              {/* mật khẩu mới */}
              <View style={styles.group}>
                <Text style={styles.label}>Mật khẩu mới <Text style={{ color: 'red' }}>*</Text></Text>
                <TextInput
                    ref={inputRefMK}
                    style={styles.input}
                    placeholder='Mật khẩu mới'
                    secureTextEntry={checkXemMK}
                    onChangeText={(text) => { setPassword(text); setPasswordMessage(""); }}
                />
                <TouchableOpacity style={styles.eyePass} onPress={() => isCheckXemMK(!checkXemMK)}>
                    <Ionicons name={checkXemMK ? 'eye-off-outline' : 'eye-outline'} size={26} />
                </TouchableOpacity>
              </View>
              <Text style={className`text-red-600 text-left`}>{passwordMessage}</Text>
    
              {/* xác nhận mật khẩu mới */}
              <View style={styles.group}>
                <Text style={styles.label}>Xác nhận mật khẩu <Text style={{ color: 'red' }}>*</Text></Text>
                <TextInput
                    ref={inputRefMK}
                    style={styles.input}
                    placeholder='Xác nhận mật khẩu'
                    secureTextEntry={checkXemMK}
                    onChangeText={(text) => { setConfirmPassword(text); setConfirmPasswordMessage(""); }}
                />
                <TouchableOpacity style={styles.eyePass} onPress={() => isCheckXemMK(!checkXemMK)}>
                    <Ionicons name={checkXemMK ? 'eye-off-outline' : 'eye-outline'} size={26} />
                </TouchableOpacity>
              </View>
              <Text style={className`text-red-600 text-left`}>{confirmPasswordMessage}</Text>
    
              {/*nút điều hướng*/}
              <TouchableOpacity onPress={clickXacNhan} style={styles.buttonLogin}>
                <Text style={styles.textLogin}>Xác nhận</Text> 
              </TouchableOpacity>

    
              {/* <TouchableOpacity
                style={className`flex justify-center items-center mt-3`}
                onPress={() => navigation.navigate('Login')}
              >
                <Text style={className`font-bold text-sm text-lime-800`}>
                  Quay lại đăng nhập
                </Text>
              </TouchableOpacity> */}
            </View>
          </ScrollView>
        </SafeAreaView>
      );
}

export default ForgotPasswordConfirm

const styles = StyleSheet.create({
  container: {
      flex: 1,
      backgroundColor: '#F5F0DC',
      paddingHorizontal: 16
  },
  image: {
      height: 180,
  },
  body: {
      marginTop: 20,
      minHeight: 100
  },
  group: {
      marginTop: 5,
      // marginHorizontal: 15,
      minHeight: 90
  },
  label: {
      fontSize: 16
  },
  input: {
      position:'relative',
      borderRadius: 10,
      height: 50,
      backgroundColor: '#fff',
      marginTop: 5,
      paddingLeft: 10,
      paddingRight:45
  },
  eyePass:{
      position:'absolute',
      right:10,
      bottom:25,
  },
  error: {
      fontSize: 14,
      color: 'red',
      marginTop: 2,
      marginBottom: -5
  },
  forgotPassword:{
      alignItems: 'flex-end',
      marginHorizontal:15,
  },
  buttonLogin:{
      height: 55,
      backgroundColor: '#53A654',
      justifyContent: 'center',
      alignItems: 'center',
      borderRadius: 10,
      marginTop: 20,
      // marginHorizontal:15,
  },
  textLogin: {
      color: '#fff',
      fontSize: 20
  },
  buttonLoginGoogle:{
      height: 55,
      backgroundColor: '#F5F0DC',
      justifyContent: 'center',
      alignItems: 'center',
      marginHorizontal:15,
      borderRadius: 10,
      borderWidth:1,
      borderColor:'#BBBBBB'
  },
  textLoginGoogle: {
      lineHeight:55,
      color: '#000',
      fontSize: 20,
  },
  logoGoogle:{
      height: 30,
      width: 30,
      marginLeft: 10,
      marginRight: 10,
  },  
  buttonRegister:{
      height: 55,
      backgroundColor: '#F5F0DC',
      justifyContent: 'center',
      alignItems: 'center',
      borderRadius: 10,
      borderWidth:1,
      borderColor:'#007bff',
      marginHorizontal:15,
      marginTop:10
  },
  textRegister: {
      fontSize: 20,
      color: '#007bff',
  }
});