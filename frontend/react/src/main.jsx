import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {ChakraProvider} from "@chakra-ui/react";
import { createStandaloneToast } from '@chakra-ui/react'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Login from "./components/login/login.jsx";
import AuthProvider from "./components/context/AuthContext.jsx";
import ProtectedRoute from "./components/sharde/protectedRoute.jsx";
import Signup from "./components/signup/Signup.jsx";
import Customer from "./Customer.jsx";
import Home from "./Home.jsx";

const { ToastContainer } = createStandaloneToast()
const router = createBrowserRouter([
    {
        path: '/',
        element: <Login />,
    },
    {
        path: '/signup',
        element: <Signup />,
    },
    {
        path: 'dashboard',
        element:
        <ProtectedRoute>
            <Home />
        </ProtectedRoute>
    },
    {
        path: 'dashboard/customers',
        element:
            <ProtectedRoute>
                <Customer />
            </ProtectedRoute>,
    }
])

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <ChakraProvider>
          <AuthProvider>
              <RouterProvider router={router} />
          </AuthProvider>
          <ToastContainer />
      </ChakraProvider>
  </StrictMode>,
)
