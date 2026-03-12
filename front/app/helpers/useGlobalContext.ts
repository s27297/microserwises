import {useContext} from "react";
import {GlobalContext} from "../GlobalProvider";

export default function useGlobalContext(){
    const context = useContext(GlobalContext);

    if (!context) {
        // to zabezpieczenie przed użyciem kontekstu poza providerem
        throw new Error("GlobalContext must be used within a GlobalProvider");
    }
return context
}