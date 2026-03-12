'use client'

import { User } from "@/app/helpers/typy/typy";
import { FaPencil, FaXmark } from "react-icons/fa6";
import { FaCheck, FaTrash } from "react-icons/fa";
import React, {Dispatch, SetStateAction, useState} from "react";
import {router} from "next/client";
import {useRouter} from "next/navigation";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    user: User;
    setUsersAction: Dispatch<SetStateAction<User[]>>;
};

export default function UserInfo({ user, setUsersAction }: Props) {
   const {router,token} = useGlobalContext();
    const [edit, setEdit] = useState(false);

    const [form, setForm] = useState({
        name: user.name||"",
        surname: user.surname||"",
        login: user.login||"",
        numerTelefonu: user.numerTelefonu||"",
        dateOfBirth: user.dateOfBirth||"",
        type: user.type||"",
        email:user.email||""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement| HTMLSelectElement>) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const deleteUser = async (id: string) => {
        try {
            await fetch(`${process.env.NEXT_PUBLIC_USERS_URI}/${id}`, {
                headers:{ Authorization: `Bearer ${token}`},
                method: "DELETE",
            });

            setUsersAction(prev => prev.filter(u => u.id !== id));
        } catch (err) {
            console.log(err);
        }
    };

    const saveUser = async () => {
             await fetch(`http://localhost/users/${user.id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
                body: JSON.stringify(form),
            })
            .then(()=>setUsersAction(prev =>
                prev.map(u => (u.id === user.id ? {...user,...form} : u))
            ))
                 .then(()=>setEdit(false))
                 .catch(err=>console.log(err))


    };

    return (
        <tr className="hover:bg-gray-100 hover:text-black">
            <td className="hover:underline hover:text-blue-600 cursor-pointer"
            onClick={()=>router.push(`/users/${user.id}`)}> {user.id}</td>
            <td>
                {edit ? (
                    <input name="name" value={form.name} onChange={handleChange}/>
                ) : (
                    user.name
                )}
            </td>

            <td >
                {edit ? (
                    <input name="surname" value={form.surname} onChange={handleChange}/>
                ) : (
                    user.surname
                )}
            </td>

            <td >
                {edit ? (
                    <input name="login" value={form.login} onChange={handleChange}/>
                ) : (
                    user.login
                )}
            </td>

            <td >
                {edit ? (
                    <input
                        type="date"
                        name="dateOfBirth"
                        value={form.dateOfBirth}
                        onChange={handleChange}
                    />
                ) : (
                    new Date(user.dateOfBirth).toLocaleDateString()
                )}
            </td>

            <td >
                {new Date(user.dateOfJoining).toLocaleDateString()}
            </td>

            <td>
                {edit ? (
                    <input
                        name="email"
                        value={form.email}
                        onChange={handleChange}
                    />
                ) : (
                    user.email
                )}
            </td>

            <td >
                {edit ? (
                    <input
                        name="numerTelefonu"
                        value={form.numerTelefonu}
                        onChange={handleChange}
                    />
                ) : (
                    user.numerTelefonu
                )}
            </td>

            <td>
                {user.type}
            </td>
            <td className={"w-12"}>
                {!edit ? (
                    <div className="flex gap-4 w-12">
                        <button onClick={() => setEdit(true)} className="text-green-600">
                            <FaPencil/>
                        </button>
                        <button
                            onClick={() => deleteUser(user.id)}
                            className="text-red-600"
                        >
                            <FaTrash/>
                        </button>
                    </div>
                ) : (
                    <div className="flex gap-4 w-12">
                        <button onClick={saveUser} className="text-green-600">
                            <FaCheck/>
                        </button>
                        <button onClick={() => setEdit(false)} className="text-red-600">
                            <FaXmark/>
                        </button>
                    </div>
                )}
            </td>
        </tr>
    );
}
