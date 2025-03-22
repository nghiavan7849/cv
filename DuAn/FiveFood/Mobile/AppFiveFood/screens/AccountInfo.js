import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, TouchableOpacity, View, ScrollView, Image } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import MenuTop from '../components/menu/MenuTop';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Icon } from 'react-native-elements';
import tw from 'twrnc';

const AccountInfo = ({ navigation }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [taiKhoan, setTaiKhoan] = useState({});

  useEffect(() => {
    const fetchSessionTaiKhoan = async () => {
      try {
        const sessionTaiKhoanStored = await AsyncStorage.getItem('sessionTaiKhoan');
        if (sessionTaiKhoanStored) {
          setIsLoggedIn(true);
          setTaiKhoan(JSON.parse(sessionTaiKhoanStored));
        } else {
          setIsLoggedIn(false);
        }
      } catch (error) {
        console.error('Lỗi khi lấy sessionTaiKhoan:', error);
      }
    };

    fetchSessionTaiKhoan();
  }, []);

  const handleLogOut = async () => {
    await AsyncStorage.removeItem('sessionTaiKhoan');
    await AsyncStorage.removeItem('sessionTokenTK');
    navigation.reset({
      index: 1,
      routes: [
        { name: 'Home' },
        { name: 'Menu' },
      ],
    });
  };

  const MenuItem = ({ title, iconName, onPress }) => (
    <TouchableOpacity style={styles.menuItem} onPress={onPress}>
      <View style={styles.menuItemContent}>
        <Icon name={iconName} type="feather" size={24} color="#6b42f5" />
        <Text style={styles.menuText}>{title}</Text>
      </View>
      <Icon name="chevron-right" type="feather" size={20} color="#b3b3b3" />
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <MenuTop title="Thông tin tài khoản" />
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        <View style={styles.profileContainer}>
          <View style={styles.avatar}>
          {taiKhoan && taiKhoan.hinhAnh ? (
            <Image
              style={tw`mx-2.5 rounded-full`}
              source={{ uri: taiKhoan.hinhAnh }}
              alt={taiKhoan.hoVaTen || "Unknown Author"}
              width={80}
              height={80}
            />
          ) : (
            <Icon name="" size={40} />

          )}
          </View>
          <Text style={styles.profileName}>{taiKhoan.hoVaTen}</Text>
        </View>

        {isLoggedIn ? (
          <>
            <View style={styles.menuSection}>
              <MenuItem
                title="Thông tin cá nhân"
                iconName="user"
                onPress={() => navigation.navigate('EditProfile')}
              />
              <MenuItem
                title="Đổi mật khẩu"
                iconName="lock"
                onPress={() => navigation.navigate('ChangePassword')}
              />
              <MenuItem
                title="Địa chỉ"
                iconName="map-pin"
                onPress={() => navigation.navigate('Address')}
              />
              <MenuItem
                title="Đơn mua"
                iconName="shopping-cart"
                onPress={() => navigation.navigate('OrderHistory')}
              />
            </View>

           

           

            <View style={styles.menuSection}>
              <MenuItem title="Đăng xuất" iconName="log-out" onPress={handleLogOut} />
            </View>
          </>
        ) : (
          <View style={styles.menuSection}>
            <MenuItem title="Đăng ký" iconName="user-plus" onPress={() => navigation.navigate('Register')} />
            <MenuItem title="Đăng nhập" iconName="log-in" onPress={() => navigation.navigate('Login')} />
            <MenuItem title="Quên mật khẩu" iconName="lock" onPress={() => navigation.navigate('ForgotPassword')} />
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
};

export default AccountInfo;

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f5efd7' },
  scrollContainer: { paddingHorizontal: 16 },
  profileContainer: {
    alignItems: 'center',
    paddingTop:20
    // paddingVertical: 20,
  },
  avatar: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: '#f1c0c0',
    justifyContent: 'center',
    alignItems: 'center',
  },
  profileName: {
    fontSize: 20,
    fontWeight: 'bold',
    marginTop: 10,
  },
  profileDescription: {
    color: '#9a9a9a',
    fontStyle: 'italic',
  },
  menuSection: {
    backgroundColor: '#fff8e1',
    borderRadius: 12,
    // paddingVertical: 10,
    marginTop: 10,
  },
  menuItem: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 15,
    borderBottomWidth: 0.5,
    borderColor: '#e0e0e0',
  },
  menuItemContent: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  menuText: {
    fontSize: 16,
    marginLeft: 16,
  },
});
