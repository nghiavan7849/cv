import React, { useCallback, useEffect, useState } from "react";
import { useDropzone } from "react-dropzone";
import { useImageUploadContext } from "./ImageUploadContext";


const UploadImgSanPham = ({ className, inputClass, onFileSelect, reset}) => {
  const { resetPreview, clearResetPreview } = useImageUploadContext();
  const [files, setFiles] = useState([]);

  const onDrop = useCallback(
    (acceptedFiles) => {
      if (acceptedFiles?.length) {
        // Hiển thị preview ảnh
        const previewFiles = acceptedFiles.map((file) =>
          Object.assign(file, { preview: URL.createObjectURL(file) })
        );
        setFiles(previewFiles);

        // Gọi callback để truyền file cho thành phần cha
        onFileSelect(acceptedFiles); // Truyền toàn bộ các file được chọn
      }
    },
    [onFileSelect]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: { "image/*": [] },
    maxFiles: 5, // Cho phép upload tối đa 5 ảnh
  });

  // Hàm xóa ảnh khỏi danh sách preview
  const handleRemoveImage = (fileName) => {
    const newFiles = files.filter(file => file.name !== fileName);  // Xóa ảnh khỏi danh sách
    setFiles(newFiles);
    onFileSelect(newFiles);  // Truyền danh sách mới lên cha
  };

  // Reset preview khi resetPreview thay đổi
  useEffect(() => {
    if (resetPreview) {
      setFiles([]); // Xóa tất cả ảnh preview
      clearResetPreview(); // Reset lại trạng thái
    }
  }, [resetPreview, clearResetPreview]);

  return (
    <div className={className}>
      <div
        {...getRootProps({ className: "dropzone" })}
        style={{
          border: "2px dashed #067a38",
          borderRadius: "4px",
          padding: "20px",
          textAlign: "center",
          cursor: "pointer",
        }}
      >
        <input {...getInputProps()} />
        {isDragActive ? (
          <p className={inputClass}>Thả ảnh ở đây ...</p>
        ) : (
          <p className={inputClass}>Kéo thả hoặc click vào để chọn ảnh</p>
        )}
      </div>

      {files.length > 0 && (
        <div
          style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "center",
            marginTop: "20px",
          }}
        >
          {files.map((file) => (
            <div
              key={file.name}
              style={{
                position: "relative",
                width: "100px",
                height: "100px",
                margin: "5px",
                borderRadius: "4px",
                overflow: "hidden",
                border: "2px solid #067a38",
              }}
            >
              <img
                src={file.preview}
                alt=""
                style={{
                  width: "100%",
                  height: "100%",
                  objectFit: "cover",
                }}
                onLoad={() => URL.revokeObjectURL(file.preview)}
              />
              {/* Button X để xóa ảnh */}
              <button
                style={{
                  position: 'absolute',
                  top: '5px',
                  right: '5px',
                  backgroundColor: 'rgba(255, 99, 71, 0.8)',  // Đỏ nhạt
                  color: 'white',
                  border: 'none',
                  borderRadius: '50%',
                  width: '24px',
                  height: '24px',
                  cursor: 'pointer',
                  fontSize: '16px',  // Tăng kích thước chữ một chút
                  fontWeight: 'bold',
                  textAlign: 'center', // Căn giữa ký tự trong nút
                  paddingBottom: '3px',  // Bỏ padding để tránh bị lệch
                  transition: 'background-color 0.3s ease',
                }}
                onMouseOver={(e) => {
                  e.target.style.backgroundColor = 'rgba(255, 99, 71, 0.6)';  // Đổi màu khi hover
                }}
                onMouseOut={(e) => {
                  e.target.style.backgroundColor = 'rgba(255, 99, 71, 0.8)';  // Trở về màu gốc khi không hover
                }}
                onClick={() => handleRemoveImage(file.name)}
              >
                &times;  {/* Dấu "×" */}
              </button>

            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UploadImgSanPham;
