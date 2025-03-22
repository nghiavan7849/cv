const formatVND = (value) => {
    return value?value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }):'';
};

export default formatVND;