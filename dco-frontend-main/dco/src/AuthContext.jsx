import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    const token = localStorage.getItem('accessToken');
    return !!token;
  });

  const login = (accessToken, refreshToken) => {
    // console.log(
    //   'Logging in with accessToken:',
    //   accessToken,
    //   'refreshToken:',
    //   refreshToken
    // );
    // localStorage.setItem('accessToken', accessToken);
    // localStorage.setItem('refreshToken', refreshToken);
    setIsLoggedIn(true);
  };

  const logout = () => {
    // localStorage.removeItem('accessToken');
    // localStorage.removeItem('refreshToken');
    setIsLoggedIn(false);
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
