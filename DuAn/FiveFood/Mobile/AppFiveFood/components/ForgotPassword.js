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
import React, { useState } from "react";
import className, { style } from "twrnc";
import { Feather } from "@expo/vector-icons";
import MenuTop from "./menu/MenuTop";
import { MaHoaMK } from "../service/MaHoaMK";
import axios from "axios";
import Toast from "react-native-toast-message";
import {ApiUrl} from '../service/ApiUrl';
import { SafeAreaView } from "react-native-safe-area-context";

const sendOTP = (email) => {
  return axios.post(`${ApiUrl}/api/tai-khoan/forgot-password/send-otp?email=` + email);
}

const ForgotPassword = ({ navigation }) => {
  const [email, setEmail] = useState("");
  const [codeOTP, setCodeOTP] = useState("");
  const [resultOTP, setResultOTP] = useState("");
  const [emailMessage, setEmailMessage] = useState("");
  const [codeOTPMessage, setCodeOTPMessage] = useState("");
  const [checkSendEmail, setCheckSendEmail] = useState(false);
  const bcrypt = require('bcryptjs');

  const clickSendOTP = async () => {
    if (checkValidateEmail()) {
      let res = await sendOTP(email);
      console.log("Đây là email: ", email);
      if (res.data.status === 'success') {
        setResultOTP(String(res.data.data.code));
        setCheckSendEmail(true);
        Toast.show({
          type: 'success',
          text1: 'Đã gửi mã xác nhận!'
        });
      } else if (res.data.status === 'error') {
        setEmailMessage(res.data.message);
      }
    }
  };
  const checkValidateEmail = () => {
    if (!email) {
      setEmailMessage("Vui lòng nhập email");
      return false;
    }
    return true;
  }
  const clickNext = async () => {
    if (!codeOTP) {
      setCodeOTPMessage("Vui lòng nhập mã xác nhận");
    }
    const codeOTPMaHoa = await MaHoaMK(codeOTP);
    const isCheckCode = bcrypt.compareSync(codeOTP, resultOTP);
    if (isCheckCode) {
      const data = {
        'email': email,
        'codeOTP': codeOTP
      }
      // navigate("/forgot-password/confirm", { state: data });
      navigation.navigate('ForgotPasswordConfirm', { email: email, codeOTP: codeOTP })
    }

  };
  const onChangeInputEmail = (e) => {
    setEmailMessage("");
    setEmail(e.target.value)
    console.log("Đây là email:", e.target.value);
  }
  const onChangeInputCodeOTP = (e) => {
    setCodeOTPMessage("");
    setCodeOTP(e.target.value)
  }

  return (
    <SafeAreaView style={[className`flex-1 bg-[#F5F0DC]`,styles.container]}>
      <MenuTop title='Quên mật khẩu' />
      <ScrollView
        contentContainerStyle={className`justify-center items-center`}
      >
        <View style={className`items-center py-12`}>
            <Image source={require('../assets/LogoFiveFoodXoaNen.png')} style={className`w-70 h-70`} />
          </View>
        <View style={className`w-full px-5`}>
          {/* <Text
            style={className`font-bold text-xl text-center mb-3 text-lime-800`}
          >
            Quên mật khẩu
          </Text> */}
          {/* Email */}
          <View
            style={className`flex-row items-center border-b border-gray-300 mb-4`}
          >
            <Feather name="mail" size={22} color={"#7C8080"} />
            <TextInput
              placeholderTextColor={"#7C8080"}
              selectionColor={"#3662AA"}
              placeholder="Email"
              style={className`flex-1 pl-3 py-2 text-base`}
              onChangeText={(text) => { setEmail(text); setEmailMessage(""); }}
              keyboardType="email-address"
              value={email}
            />
          </View>
          <Text style={className`text-red-600 text-left`}>{emailMessage}</Text>

          {/* Mã xác nhận và nút Gửi OTP */}
          <View style={className`flex-row items-center mb-4`}>
            <View
              style={className`flex-1 flex-row items-center border-b border-gray-300`}
            >
              <Feather name="key" size={22} color={"#7C8080"} />
              <TextInput
                placeholderTextColor={"#7C8080"}
                selectionColor={"#3662AA"}
                placeholder="Mã xác nhận"
                style={className`flex-1 pl-3 py-2 text-base`}
                onChangeText={(text) => { setCodeOTP(text); setCodeOTPMessage(""); console.log(codeOTP) }}
                value={codeOTP}
              />
            </View>
            <TouchableOpacity
              style={className`bg-green-600 rounded-full py-3 px-4.5 ml-3`}
              onPress={clickSendOTP}
            >
              <Text style={className`text-white font-bold`}>Gửi OTP</Text>
            </TouchableOpacity>
          </View>
          <Text style={className`text-red-600 text-left`}>{codeOTPMessage}</Text>

          <TouchableOpacity
            style={className`flex justify-center items-center my-5 ${checkSendEmail ? '' : 'opacity-50'}`}
            onPress={clickNext}
            disabled={checkSendEmail ? false : true}
          >
            <View style={className`w-[100%] py-5 bg-green-600 rounded-full py-3`}>
              <Text style={className`font-bold text-xl text-white text-center`}>
                Xác nhận
              </Text>
            </View>
          </TouchableOpacity>

          {/* <TouchableOpacity
            style={className`flex justify-center items-center`}
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
};

const styles = StyleSheet.create({
  container: {
      flex: 1,
      backgroundColor: '#F5F0DC',
      paddingHorizontal: 16
    }
  });

export default ForgotPassword;
