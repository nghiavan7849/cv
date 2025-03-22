import React, { createContext, useState, useContext } from "react";

const ImageUploadContext = createContext();

export const useImageUploadContext = () => useContext(ImageUploadContext);

export const ImageUploadProvider = ({ children }) => {
  const [resetPreview, setResetPreview] = useState(false);

  // Hàm để reset preview
  const resetImagePreview = () => setResetPreview(true);
  const clearResetPreview = () => setResetPreview(false);

  return (
    <ImageUploadContext.Provider
      value={{ resetPreview, resetImagePreview, clearResetPreview }}
    >
      {children}
    </ImageUploadContext.Provider>
  );
};
