import * as Crypto from 'expo-crypto';

export const MaHoaMK = async (password) => {
    try {
        // Mã hóa mật khẩu sử dụng SHA-256
        const hash = await Crypto.digestStringAsync(Crypto.CryptoDigestAlgorithm.SHA256, password);
        return hash;
    } catch (err) {
        console.log(err);
        throw new Error('Lỗi mã hóa mật khẩu');
    }
};
