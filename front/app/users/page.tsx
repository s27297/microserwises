'use client'

import useGlobalContext from "@/app/helpers/useGlobalContext";
import {useEffect, useState} from "react";
import {User} from "@/app/helpers/typy/typy";
import UserInfo from "@/app/users/UserInfo";
export default function Users() {
    const {router,token}=useGlobalContext()
    const [users,setUsers] = useState<User[]>([])
console.log(users)
    useEffect(()=>{
        if(!token) return;
        console.log(token);
        const getUsers=async ()=>{
            fetch(`${process.env.NEXT_PUBLIC_USERS_URI}`,{
                method:"GET",
                headers:{ Authorization: `Bearer ${token}`}
            })
                .then(res=>res.json())
                .then(res=> {
                    if (Array.isArray(res)) setUsers(res)
                })
                .catch((err)=>console.log(err))
        }
        getUsers()

    },[token])
    if(!users||!users.length)
        return <div>
            <button onClick={() => router.push("/")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >return
            </button>
            <button onClick={() => router.push("/users/create-user")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >utworz użytkownika
            </button>
            <p>użytkownicy nie znalażone</p></div>
    return (
        <div>
            <button onClick={() => router.push("/")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >return
            </button>
            <button onClick={() => router.push("/users/create-user")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >utworz użytkownika
            </button>
            <table className="w-full border-collapse mt-4">
                <thead>
                <tr className="bg-gray-200 text-black">
                    <th className="border px-2 py-1">Id</th>
                    <th className="border px-2 py-1">Name</th>
                    <th className="border px-2 py-1">Surname</th>
                    <th className="border px-2 py-1">Login</th>
                    <th className="border px-2 py-1">Date of Birth</th>
                    <th className="border px-2 py-1">Date of Joining</th>
                    <th className="border px-2 py-1">Email</th>
                    <th className="border px-2 py-1">Phone</th>
                    <th className="border px-2 py-1">Typ</th>
                    <th className="border px-2 py-1 w-12">Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user, index) => (
                    <UserInfo key={index} user={user} setUsersAction={setUsers}/>
                ))}
                </tbody>
            </table>
        </div>
    )
}