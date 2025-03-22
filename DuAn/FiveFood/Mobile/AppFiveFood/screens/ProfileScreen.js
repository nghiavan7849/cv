// screens/ProfileScreen.js
import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';

export default function ProfileScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={() => navigation.goBack()}>
        <Text style={styles.backButton}>Quay lại</Text>
      </TouchableOpacity>
      <Text style={styles.title}>Personal Info</Text>
      <TouchableOpacity onPress={() => navigation.navigate('EditProfile')}>
        <Text style={styles.editButton}>EDIT</Text>
      </TouchableOpacity>
      <View style={styles.profileInfo}>
        <Text style={styles.infoLabel}>Full Name</Text>
        <Text style={styles.infoText}>Vishal Khadok</Text>
        <Text style={styles.infoLabel}>Email</Text>
        <Text style={styles.infoText}>hello@halolab.co</Text>
        <Text style={styles.infoLabel}>Phone Number</Text>
        <Text style={styles.infoText}>408-841-0926</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f9f4e8', padding: 16, marginTop:50 },
  backButton: { fontSize: 16, color: '#000' },
  title: { fontSize: 24, fontWeight: 'bold', marginBottom: 20 },
  editButton: { fontSize: 16, color: 'orange', textAlign: 'right' },
  profileInfo: { marginTop: 20 },
  infoLabel: { fontSize: 14, color: '#888', marginVertical: 5 },
  infoText: { fontSize: 18, color: '#000' },
});
