'use client'

import React, { useEffect, useState } from "react";
import {useParams, useRouter} from "next/navigation";
import { User } from "@/app/helpers/typy/typy";
import useGlobalContext from "@/app/helpers/useGlobalContext";
import {FaCheck, FaPlus, FaTrash} from "react-icons/fa";
import SprawnoscInfo from "@/app/users/[id]/SprawnoscInfo";
import PaymentInfo from "@/app/users/[id]/PaymentInfo";

export default function UserDetailsPage() {
    const {router,token} = useGlobalContext();
    const { id } = useParams();
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    useEffect(() => {
        if(!token)return
        const fetchUser = async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_USERS_URI}/users/${id}`,{
                    headers:{Authorization: `Bearer ${token}`}
                });
                const data = await res.json();
                setUser(data);
            } catch (err) {
                console.log(err);
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, [id,token]);

    if (loading) return <div>Ładowanie...</div>;
    if (!user) return <div>      <button onClick={() => router.push("/users")}
                                         className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
    >return
    </button>
        <p>Nie znaleziono użytkownika</p></div>;

    return (
        <div className="max-w-4xl mx-auto p-6 space-y-6">
            <button onClick={() => router.push("/users")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >return
            </button>
            {/* BASIC INFO */}
            <section className="border rounded p-4">
                <h2 className="text-xl font-bold mb-2">Dane użytkownika</h2>
                <div className="grid grid-cols-2 gap-2">
                    <div><b>Imię:</b> {user.name}</div>
                    <div><b>Nazwisko:</b> {user.surname}</div>
                    <div><b>Login:</b> {user.login}</div>
                    <div><b>Email:</b> {user.email}</div>
                    <div><b>Telefon:</b> {user.numerTelefonu}</div>
                    <div><b>Typ:</b> {user.type}</div>
                    <div>
                        <b>Data urodzenia:</b>{" "}
                        {new Date(user.dateOfBirth).toLocaleDateString()}
                    </div>
                    <div>
                        <b>Data dołączenia:</b>{" "}
                        {new Date(user.dateOfJoining).toLocaleDateString()}
                    </div>
                </div>
            </section>

            {/* SPRAWNOŚCI */}
            <section className="border rounded p-4">
                <div className={"flex flex-row gap-2 mb-2"}>
                <h2 className="text-xl font-bold">Sprawności</h2>
                    <button onClick={()=>router.push(`/users/${id}/add-sprawnosc`)}>
                        <FaPlus className={"text-green-600"}/></button>
                </div>
                {user.sprawnosci?.length > 0 ? (
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-200 text-black">
                            <th className="border px-2 py-1">ID</th>
                            <th className="border px-2 py-1">Typ</th>
                            <th className="border px-2 py-1">Nazwa</th>
                            <th className="border px-2 py-1w-12">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {user.sprawnosci.map(s => (
                            <SprawnoscInfo sprawnosc={s} setUserAction={setUser} id={id} key={s.source_id}/>
                        ))}
                        </tbody>
                    </table>
                ):(
                    <div>Brak sprawności</div>
                ) }
            </section>

            {/* PAYMENTS */}
            <section className="border rounded p-4">
                <div className={"flex flex-row gap-2 mb-2"}>
                    <h2 className="text-xl font-bold ">Wpłaty</h2>
                    <button onClick={() => router.push(`/users/${id}/add-payment`)}>
                        <FaPlus className={"text-green-600"}/></button>
                </div>
                {user.payments?.length > 0 ? (
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-200 text-black">
                            <th className="border px-2 py-1">ID</th>
                            <th className="border px-2 py-1">Nazwa</th>
                            <th className="border px-2 py-1">Data</th>
                            <th className="border px-2 py-1">Kwota</th>
                            <th className="border px-2 py-1w-12">Actions</th>

                        </tr>
                        </thead>
                        <tbody>
                        {user.payments.map(p => (
                           <PaymentInfo payment={p} setUserAction={setUser} id={id} key={p.source_id}/>
                        ))}
                        </tbody>
                    </table>
                ):(
                    <div>Brak wpłat</div>
                ) }
            </section>
        </div>
    );
}
