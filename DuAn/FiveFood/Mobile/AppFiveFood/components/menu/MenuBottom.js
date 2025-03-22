import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Home from '../Home';
import Cart from '../Cart';
import ChatScreen from '../Register'; // Assuming you have a ChatScreen component
import NewsScreen from '../ChangePassword'; // Assuming you have a NewsScreen component
import MoreScreen from '../../screens/AccountInfo'; // Assuming you have a MoreScreen component
import Icon from 'react-native-vector-icons/Ionicons'; // Changed to Ionicons for the outline icons
import OrderHistory from '../OrderHistory';
import Chat from '../Chat';
import AccountInfo from '../../screens/AccountInfo';
import News from '../News';
const Tab = createBottomTabNavigator();

const MenuBottom = () => {
  return (
    <Tab.Navigator
      initialRouteName='Trang chủ'
      screenOptions={{
        headerShown: false,
        tabBarActiveTintColor: 'green', // Set active tab icon color to green
        tabBarInactiveTintColor: 'gray', // Set inactive tab icon color to gray
      }}
    >
      <Tab.Screen 
        name="Trang chủ" 
        component={Home} 
        options={{
          tabBarIcon: ({ color }) => <Icon name="home-outline" size={24} color={color} />
        }} 
      />
      <Tab.Screen 
        name="Giỏ hàng" 
        component={Cart} 
        options={{
          tabBarIcon: ({ color }) => <Icon name="cart-outline" size={24} color={color} />
        }} 
      />
      <Tab.Screen 
        name="Chat" 
        component={Chat} 
        options={{
          tabBarIcon: ({ color }) => <Icon name="chatbubbles-outline" size={24} color={color} />
        }} 
      />
      <Tab.Screen 
        name="Khác" 
        component={AccountInfo} 
        options={{
          tabBarIcon: ({ color }) => <Icon name="ellipsis-horizontal-outline" size={24} color={color} />
        }} 

      />
    </Tab.Navigator>
  );
};

export default MenuBottom;
