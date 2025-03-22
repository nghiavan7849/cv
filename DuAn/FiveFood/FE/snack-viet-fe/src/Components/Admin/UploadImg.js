import React, { useCallback, useEffect, useState } from 'react';
import { useDropzone } from 'react-dropzone';

const UploadImg = ({ className, inputClass, onFileSelect, reset }) => {
  const [files, setFiles] = useState([]);

  const onDrop = useCallback((acceptedFiles) => {
    if (acceptedFiles?.length) {
      // Hiển thị preview ảnh
      const previewFiles = acceptedFiles.map(file =>
        Object.assign(file, { preview: URL.createObjectURL(file) })
      );
      setFiles(previewFiles);

      // Gọi callback để truyền file cho thành phần cha
      onFileSelect(acceptedFiles[0]);  // Chỉ chọn file đầu tiên
    }
  }, [onFileSelect]);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: { 'image/*': [] },
    maxFiles: 1,
  });

  // Reset preview khi reset prop thay đổi
  useEffect(() => {
    if (reset) {
      setFiles([]);
    }
  }, [reset]);

  return (
    <div className={className}>
      <div
        {...getRootProps({ className: 'dropzone' })}
        style={{
          border: '2px dashed #007bff',
          borderRadius: '4px',
          padding: '20px',
          textAlign: 'center',
          cursor: 'pointer'
        }}
      >
        <input {...getInputProps()} />
        {
          isDragActive ?
            <p className={inputClass}>Thả ảnh ở đây ...</p> :
            <p className={inputClass}>Kéo thả hoặc click vào để chọn ảnh</p>
        }
      </div>

      {files.length > 0 && (
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          marginTop: '20px'
        }}>
          {files.map(file => (
            <div key={file.name} style={{
              position: 'relative',
              width: '120px',
              height: '120px',
              borderRadius: '50%',
              overflow: 'hidden',
              border: '2px solid #007bff'
            }}>
              <img src={file.preview} alt=""
                style={{
                  width: '100%',
                  height: '100%',
                  objectFit: 'cover'
                }}
                onLoad={() => URL.revokeObjectURL(file.preview)}
              />
            </div>
          ))}
        </div>
      )}
    </div>
  );
};


export default UploadImg;
