import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import className from 'twrnc';

const Sidebar = ({ isVisible, onClose, sessionTaiKhoan, navigation }) => {

  if (!isVisible) return null; // Không render nếu sidebar không được mở

  if (Object.keys(sessionTaiKhoan).length === 0) {
    return (
      <View style={className`absolute top-0 left-0 bg-white w-3/4 h-full shadow-lg z-10`}>
        {/* Header với nút đóng */}
        <View style={className`p-5 flex-row justify-between items-center`}>
          <Text style={className`text-lg font-semibold`}>Thông tin tài khoản</Text>
          <TouchableOpacity onPress={onClose}>
            <Text style={className`text-red-600 font-bold`}>X</Text>
          </TouchableOpacity>
        </View>

        {/* Nội dung sidebar */}
        <View style={className`p-5`}>
          <TouchableOpacity>
            <Text style={className`text-base`} onPress={() => navigation.navigate('/Register')}>Đăng ký</Text>
          </TouchableOpacity>
          <Text style={className`text-base mt-2`} onPress={() => navigation.navigate('/Login')}>Đăng nhập</Text>
          <Text style={className`text-base mt-2`} onPress={() => navigation.navigate('/ForgotPassword')}>Quên mật khẩu</Text>
          {/* Các thông tin khác có thể thêm ở đây */}
        </View>
      </View>
    );
  } else {
    return (
      <View style={className`absolute top-0 left-0 bg-white w-3/4 h-full shadow-lg z-10`}>
        {/* Header với nút đóng */}
        <View style={className`p-5 flex-row justify-between items-center`}>
          <Text style={className`text-lg font-semibold`}>Thông tin tài khoản</Text>
          <TouchableOpacity onPress={onClose}>
            <Text style={className`text-red-600 font-bold`}>X</Text>
          </TouchableOpacity>
        </View>

        {/* Nội dung sidebar */}
        <View style={className`p-5`}>
          <Text style={className`text-base`}>Cập nhật thông tin</Text>
          <Text style={className`text-base mt-2`}>Đổi mật khẩu</Text>
          <Text style={className`text-base mt-2`}>Đăng xuất</Text>
          {/* Các thông tin khác có thể thêm ở đây */}
        </View>
      </View>
    );
  }
};

export default Sidebar;
