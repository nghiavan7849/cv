import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Home from './components/Home';
import ProductDetail from './components/ProductDetail';
import Welcome from './components/Welcome';
import AccountInfo from './screens/AccountInfo';
import Register from './components/Register';
import Login from './components/Login';
import ForgotPassword from './components/ForgotPassword';
import EditEvaluate from './components/EditEvaluate';
import Evaluate from './components/Evaluate';
import SeeReviews from './components/SeeReviews';
import ChangePassword from './components/ChangePassword';
import ForgotPasswordConfirm from './components/ForgotPasswordConfirm';
import Cart from './components/Cart';
import OrderHistory from './components/OrderHistory';
import AddressScreen from './components/Address';
import AddAddressScreen from './components/AddAddress';
import UpdateAddressScreen from './components/UpdateAddress';
import Toast from 'react-native-toast-message';
import MenuBottom from './components/menu/MenuBottom';
import DetailOrderHistory from './components/DetailOrderHistory';
import ProfileScreen from './screens/ProfileScreen';
import EditProfile from './screens/EditProfile';
import PayDetail from './components/PayDetail';
import OrderConfirmation from './components/OrderConfirmation';
import { Component } from 'react';
import Chat from './components/Chat';
import News from './components/News';
import ChatBot from './components/ChatBot';
import QRCode from './components/QRCode';

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <>
      <NavigationContainer >
        <Stack.Navigator initialRouteName="Welcome" screenOptions={{
          cardStyle: { backgroundColor: '#F5F0DC' },
          headerShown: false
        }}>
          <Stack.Screen name="Welcome" component={Welcome} options={{ headerShown: false }} />
          <Stack.Screen name="Menu" component={MenuBottom} options={{ headerShown: false }} />
          <Stack.Screen name="Home" component={Home} options={{ headerShown: false }} />
          <Stack.Screen name="ProductDetail" component={ProductDetail} options={{ headerShown: false }} />
          <Stack.Screen name="AccountInfo" component={AccountInfo} options={{ headerShown: false }} />
          <Stack.Screen name="ProfileScreen" component={ProfileScreen} options={{ headerShown: false }} />
          <Stack.Screen name="EditProfile" component={EditProfile} options={{ headerShown: false }} />
          <Stack.Screen name="EditEvaluate" component={EditEvaluate} options={{ headerShown: false }} />
          <Stack.Screen name="Evaluate" component={Evaluate} options={{ headerShown: false }} />
          <Stack.Screen name="SeeReviews" component={SeeReviews} options={{ headerShown: false }} />
          <Stack.Screen name="Register" component={Register} options={{ headerShown: false }} />
          <Stack.Screen name="Login" component={Login} options={{ headerShown: false }} />
          <Stack.Screen name="ForgotPassword" component={ForgotPassword} options={{ title: 'Quên mật khẩu' }} />
          <Stack.Screen name="ForgotPasswordConfirm" component={ForgotPasswordConfirm} options={{ title: 'Xác nhận mật khẩu' }} />
          <Stack.Screen name="ChangePassword" component={ChangePassword} options={{ headerShown: false }} />
          <Stack.Screen name="Cart" component={Cart} options={{ headerShown: false }} />
          <Stack.Screen name="OrderHistory" component={OrderHistory} options={{ headerShown: false }} />
          <Stack.Screen name="OrderConfirmation" component={OrderConfirmation} options={{ headerShown: false }} />
          <Stack.Screen name="DetaillOrderHistory" component={DetailOrderHistory} options={{ headerShown: false }} />
          <Stack.Screen name="Address" component={AddressScreen} options={{ headerShown: false }} />
          <Stack.Screen name="AddAddress" component={AddAddressScreen} options={{ headerShown: false }} />
          <Stack.Screen name="UpdateAddress" component={UpdateAddressScreen} options={{ headerShown: false }} />
          <Stack.Screen name="PayDetail" component={PayDetail} options={{ headerShown: false }} />
          <Stack.Screen name="Chat" component={Chat} options={{ headerShown: false }} />
          <Stack.Screen name="ChatBot" component={ChatBot} options={{ headerShown: false }} />
          <Stack.Screen name="News" component={News} options={{ headerShown: false }} />
          <Stack.Screen name="QRCode" component={QRCode} options={{ headerShown: false }} />
        </Stack.Navigator>

      </NavigationContainer>
      <Toast />
    </>
  );
}
