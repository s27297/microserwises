'use client'

import {createContext, useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import { keycloak } from "@/app/helpers/keyckloak";

type GlobalContextType = {
    router: ReturnType<typeof useRouter>;
    token?: string;
};

export const GlobalContext=createContext<GlobalContextType | undefined>(undefined)

export default function GlobalProvider({children}:{
    children: React.ReactNode;
}){
    const router=useRouter()
    const [token, setToken] = useState<string | undefined>(undefined);

    useEffect(()=>{
        keycloak.init({
            onLoad: "login-required",
            pkceMethod: "S256",
            checkLoginIframe: false
        }).then(authenticated => {
            if (!authenticated) {
                window.location.reload();
            }
            else {
                setToken(keycloak.token)
                fetch(`${process.env.NEXT_PUBLIC_USERS_URI}`,{
                    method:"GET",
                    headers:{ Authorization: `Bearer ${token}`}
                }).then(res=>res.json())
                    .then(console.log)

            }
        })

    },[])
    useEffect(() => {

       const interval=setInterval(() => {
           keycloak.updateToken(30).then(refreshed => {
               if (refreshed) {
                   setToken(keycloak.token);
               }
           })
       .catch(() => keycloak.login());
        }, 10000);
             return () => clearInterval(interval);

    }, []);
    return ( <GlobalContext.Provider value={{
            router,token
    }}>{children}</GlobalContext.Provider>
    )
}