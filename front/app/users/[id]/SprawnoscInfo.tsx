'use client'

import React, { Dispatch, SetStateAction, useState } from "react";
import { FaCheck, FaTrash } from "react-icons/fa";
import { FaPencil, FaXmark } from "react-icons/fa6";
import { Sprawnosc, User } from "@/app/helpers/typy/typy";
import {ParamValue} from "next/dist/server/request/params";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    sprawnosc: Sprawnosc;
    setUserAction: Dispatch<SetStateAction<User | null>>;
    id: ParamValue;
};

export default function SprawnoscInfo({ sprawnosc, setUserAction, id }: Props) {
    const [editSprawnosc, setEditSprawnosc] = useState(false);

    const [sprawnoscForm, setSprawnoscForm] = useState({
        sprawnosc_type: sprawnosc.sprawnosc_type,
        sprawnosc_name: sprawnosc.sprawnosc_name,
    });
    const {token}=useGlobalContext();
    const deleteSprawnosc = async () => {
        await fetch(
            `${process.env.NEXT_PUBLIC_USERS_URI}/sprawnosc?id=${id}&source_id=${sprawnosc.source_id}`,
            { method: "DELETE",
            headers:{ Authorization: `Bearer ${token}`}}
        );

        setUserAction(prev =>
            prev
                ? {
                    ...prev,
                    sprawnosci: prev.sprawnosci.filter(
                        s => s.source_id !== sprawnosc.source_id
                    ),
                }
                : prev
        );
    };

    const saveSprawnosc = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_USERS_URI}/sprawnosc/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
            body: JSON.stringify({
                source_id: sprawnosc.source_id,
                ...sprawnoscForm,
            }),
        });

        setUserAction(prev =>
            prev
                ? {
                    ...prev,
                    sprawnosci: prev.sprawnosci.map(s =>
                        s.source_id === sprawnosc.source_id
                            ? { ...s, ...sprawnoscForm }
                            : s
                    ),
                }
                : prev
        );

        setEditSprawnosc(false);
    };

    return (
        <tr>
            <td className="border px-2 py-1">{sprawnosc.source_id}</td>

            <td className="border px-2 py-1">
                {editSprawnosc ? (
                    <input
                        value={sprawnoscForm.sprawnosc_type}
                        onChange={e =>
                            setSprawnoscForm(f => ({
                                ...f,
                                sprawnosc_type: e.target.value,
                            }))
                        }
                        className="border px-1 w-full"
                    />
                ) : (
                    sprawnosc.sprawnosc_type
                )}
            </td>

            <td className="border px-2 py-1">
                {editSprawnosc ? (
                    <input
                        value={sprawnoscForm.sprawnosc_name}
                        onChange={e =>
                            setSprawnoscForm(f => ({
                                ...f,
                                sprawnosc_name: e.target.value,
                            }))
                        }
                        className="border px-1 w-full"
                    />
                ) : (
                    sprawnosc.sprawnosc_name
                )}
            </td>

            <td className="border px-2 py-1">
                <div className={"flex flex-row  gap-3 "}>
                    {editSprawnosc ? (
                        <>
                            <FaCheck
                                className="text-green-600 cursor-pointer "
                                onClick={saveSprawnosc}
                            />
                            <FaXmark
                                className="text-red-600 cursor-pointer"
                                onClick={() => setEditSprawnosc(false)}
                            />
                        </>
                    ) : (
                        <>
                            <FaPencil
                                className="text-blue-600 cursor-pointer"
                                onClick={() => setEditSprawnosc(true)}
                            />
                            <FaTrash
                                className="text-red-600 cursor-pointer"
                                onClick={deleteSprawnosc}
                            />
                        </>
                    )}
                </div>
            </td>
        </tr>
    );
}
