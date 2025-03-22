import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import React, { useState } from "react";
import { Feather } from "@expo/vector-icons";
import className from "twrnc";
import MenuTop from "../components/menu/MenuTop";

const Register = ({ navigation }) => {
  const [fullName, setFullName] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleRegister = () => {
    // Xử lý đăng ký ở đây
    console.log("Full Name:", fullName);
    console.log("Username:", username);
    console.log("Email:", email);
    console.log("Password:", password);
  };

  return (
    <SafeAreaView style={className`flex-1 bg-[#F5F0DC]`}>
      {/* <StatusBar /> */}
      <MenuTop title={'Đăng ký'}/>
      <ScrollView
        contentContainerStyle={className`flex-1 justify-center items-center`}
      >
        <View style={className`w-full px-5`}>
          <Text
            style={className`font-bold text-xl text-center mb-3 text-lime-800`}
          >
            Đăng ký
          </Text>

          {/* Họ và tên */}
          <View
            style={className`flex-row items-center border-b border-gray-300 mb-4`}
          >
            <Feather name="user" size={22} color={"#7C8080"} />
            <TextInput
              placeholderTextColor={"#7C8080"}
              selectionColor={"#3662AA"}
              placeholder="Họ và tên"
              style={className`flex-1 pl-3 py-2 text-base`}
              onChangeText={setFullName}
              value={fullName}
            />
          </View>

          {/* Tên đăng nhập */}
          <View
            style={className`flex-row items-center border-b border-gray-300 mb-4`}
          >
            <Feather name="user" size={22} color={"#7C8080"} />
            <TextInput
              placeholderTextColor={"#7C8080"}
              selectionColor={"#3662AA"}
              placeholder="Tên đăng nhập"
              style={className`flex-1 pl-3 py-2 text-base`}
              onChangeText={setUsername}
              value={username}
            />
          </View>

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
              onChangeText={setEmail}
              value={email}
            />
          </View>

          {/* Mật khẩu */}
          <View
            style={className`flex-row items-center border-b border-gray-300 mb-4`}
          >
            <Feather name="lock" size={22} color={"#7C8080"} />
            <TextInput
              placeholderTextColor={"#7C8080"}
              selectionColor={"#3662AA"}
              placeholder="Mật khẩu"
              secureTextEntry
              style={className`flex-1 pl-3 py-2 text-base`}
              onChangeText={setPassword}
              value={password}
            />
          </View>

          {/* Xác nhận mật khẩu */}
          <View
            style={className`flex-row items-center border-b border-gray-300 mb-4`}
          >
            <Feather name="lock" size={22} color={"#7C8080"} />
            <TextInput
              placeholderTextColor={"#7C8080"}
              selectionColor={"#3662AA"}
              placeholder="Xác nhận mật khẩu"
              secureTextEntry
              style={className`flex-1 pl-3 py-2 text-base`}
              onChangeText={setConfirmPassword}
              value={confirmPassword}
            />
          </View>

          <TouchableOpacity
            style={className`flex justify-center items-center my-5`}
            onPress={handleRegister}
          >
            <View style={className`w-32 bg-green-600 rounded-full py-3`}>
              <Text style={className`font-bold text-xl text-white text-center`}>
                Đăng ký
              </Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity
            style={className`flex justify-center items-center`}
            onPress={() => navigation.navigate("Login")}
          >
            <Text style={className`font-bold text-sm text-lime-800`}>
              Đã có tài khoản? Đăng nhập ngay!
            </Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

export default Register;
