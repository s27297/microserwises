'use client'

import { useState } from "react";
import { Wydarzenie } from "@/app/helpers/typy/typy";
import { FaPencil, FaTrash, FaCheck, FaXmark } from "react-icons/fa6";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    wydarzenie: Wydarzenie;
    setEvents: React.Dispatch<React.SetStateAction<Wydarzenie[]>>;
};

export default function WydarzenieInfo({ wydarzenie, setEvents }: Props) {
    const [edit, setEdit] = useState(false);
    const [form, setForm] = useState({ ...wydarzenie });
    const {token}=useGlobalContext()
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const save = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_EVENTS_URI}/${wydarzenie.id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify(form),
        });

        setEvents(prev =>
            prev.map(e => e.id === wydarzenie.id ? form : e)
        );
        setEdit(false);
    };

    const remove = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_EVENTS_URI}/${wydarzenie.id}`, {
            method: "DELETE",
            headers:{Authorization: `Bearer ${token}`}
        });

        setEvents(prev => prev.filter(e => e.id !== wydarzenie.id));
    };

    return (
        <tr className="hover:bg-gray-100 hover:text-black">
            <td className="border px-2 py-1">{wydarzenie.id}</td>

            <td className="border px-2 py-1">
                {edit ? (
                    <input name="nazwa" value={form.nazwa} onChange={handleChange}
                           className="border px-1 py-0.5 w-full"/>
                ) : wydarzenie.nazwa}
            </td>

            <td className="border px-2 py-1">
                {edit ? (
                    <input
                        type="datetime-local"
                        name="dataWyjazdu"
                        value={form.dataWyjazdu}
                        onChange={handleChange}
                        className="border px-1 py-0.5"
                    />
                ) : new Date(wydarzenie.dataWyjazdu).toLocaleString()}
            </td>

            <td className="border px-2 py-1">
                {edit ? (
                    <input
                        type="datetime-local"
                        name="dataZakonczenia"
                        value={form.dataZakonczenia}
                        onChange={handleChange}
                        className="border px-1 py-0.5"
                    />
                ) : new Date(wydarzenie.dataZakonczenia).toLocaleString()}
            </td>

            <td className="border px-2 py-1">
                {edit ? (
                    <textarea
                        name="opis"
                        value={form.opis}
                        onChange={handleChange}
                        className="border px-1 py-0.5 w-full"
                    />
                ) : wydarzenie.opis}
            </td>

            <td className="border px-2 py-1">
                { wydarzenie.organizatorId}
            </td>

            <td className="border px-2 py-1">
                {edit ? (
                    <div className="flex gap-2 ">
                        <button disabled={!form.nazwa||!form.opis||!form.dataWyjazdu||!form.dataZakonczenia
                            ||new Date(form.dataZakonczenia).getTime()<new Date(form.dataWyjazdu).getTime()}>
                            <FaCheck className="text-green-600 cursor-pointer" onClick={save}
                        /></button>
                        <FaXmark className="text-red-600 cursor-pointer" onClick={() => setEdit(false)}/>
                    </div>
                ) : (
                    <div className="flex gap-2 ">
                        <FaPencil className="text-blue-600 cursor-pointer" onClick={() => setEdit(true)}/>
                        <FaTrash className="text-red-600 cursor-pointer" onClick={remove}/>
                    </div>
                )}
            </td>
        </tr>
    );
}
