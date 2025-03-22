import React from 'react';
import { View, Text, TouchableOpacity, ScrollView } from 'react-native';
import className from 'twrnc';
import Icon from 'react-native-vector-icons/MaterialIcons';
import { SafeAreaView } from 'react-native-safe-area-context';

const OrderConfirmationScreen = ({navigation}) => {
  return (
    <SafeAreaView style={className`flex-1 bg-[#F5F0DC]`}>
      <View style={className`flex-1 justify-center items-center`}>
        <View style={className`py-3 px-6 items-center `}>
          <Icon name="check-circle" size={48} color="#067A38" />
          <Text style={className`text-2xl font-bold text-center mt-4`}>Đặt hàng thành công</Text>
          <Text style={className`text-[#4a4a4a] text-lg text-center mt-2`}>Cảm ơn bạn đã mua hàng</Text>
        </View>
        <TouchableOpacity onPress={()=>navigation.navigate("OrderHistory")}>
            <Text style={className`border border-red-500 rounded-md max-h-20 py-2.5 px-6 text-red-500 text-center text-lg font-bold`}>Xem đơn hàng</Text>
        </TouchableOpacity> 
      </View>
      <View contentContainerStyle={className`flex-1 justify-between`}>
        <View style={className`flex-1`} /> 
        <View style={className`flex-row justify-between w-full py-4 px-6`}>
          <TouchableOpacity style={className`bg-[#067A38] py-4 rounded-lg flex-1 mr-2`} onPress={()=>navigation.navigate("Menu")}>
            <Text style={className`text-white text-center text-lg font-bold`}>Tiếp tục mua sắm</Text>
          </TouchableOpacity>
        </View>
      </View>
    </SafeAreaView>
  );
};

export default OrderConfirmationScreen;