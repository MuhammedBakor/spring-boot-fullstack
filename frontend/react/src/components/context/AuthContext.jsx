import {
    createContext,
    useContext,
    useEffect,
    useState
} from "react";

import {login as performLogin} from "../../services/client.js";
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext({})

const AuthProvider = ({ children }) => {

    const [customer, setCustomer] = useState(null);

    const setCustomFromToken = () => {
        let token = localStorage.getItem("access_token");
        if (token) {
            token = jwtDecode(token);
            setCustomer({
                username: token.sub,
                roles: token.scops,
            });
        }
    }

    useEffect(() => {
        setCustomFromToken()
    }, [])

    const login = async (usernameAndPassword) =>{
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword)
            .then(res => {
               const jwtToken = res.headers["authorization"];
               localStorage.setItem("access_token", jwtToken);

               const decodeToken = jwtDecode(jwtToken);

               setCustomer({
                    username: decodeToken.sub,
                    roles: decodeToken.scops,
                });

                resolve(res);
            }).catch(error => {
                reject(error);
            });
        })
    }

    const logout = () => {
        if (localStorage.getItem("access_token") != null) {
            localStorage.removeItem("access_token");
            setCustomer(null);
        }
        else throw Error("Not logged in");
    }

    const isCustomerAuthenticated = () => {
        const token = localStorage.getItem("access_token");
        if (!token) {
            return false;
        }
        const {exp: expiration} = jwtDecode(token);
        if(Date.now() > expiration * 1000) {
            logout()
            return false;
        }
        return true;
    }

    return (
        <AuthContext.Provider value={{
        customer,
        login,
        logout,
        isCustomerAuthenticated,
            setCustomFromToken
        }}>
            {children}
        </AuthContext.Provider>
    )

}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;