'use client'

import { useParams, useRouter } from "next/navigation";
import React, { useState } from "react";
import useGlobalContext from "@/app/helpers/useGlobalContext";

export default function AddSprawnoscPage() {
    const { id } = useParams();
    const {router,token} = useGlobalContext();

    const [form, setForm] = useState({
        sprawnosc_type: "",
        sprawnosc_name: "",
        source_id: "",
    });

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement>
    ) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            await fetch(`${process.env.NEXT_PUBLIC_USERS_URI}/sprawnosc/${id}`, {
                method: "POST",
                headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
                body: JSON.stringify(form),
            });

            router.push(`/users/${id}`);
        } catch (err) {
            console.log(err);
        }
    };

    return (
        <div className="max-w-md mx-auto p-6">
            <h1 className="text-xl font-bold mb-4">Dodaj sprawność</h1>

            <form onSubmit={handleSubmit} className="space-y-3">

                <input
                    name="source_id"
                    placeholder="source id"
                    value={form.source_id}
                    onChange={handleChange}
                    type={"number"}
                    className="w-full border px-2 py-1 rounded"
                    required
                />
                <input
                    name="sprawnosc_type"
                    placeholder="Typ sprawności"
                    value={form.sprawnosc_type}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />

                <input
                    name="sprawnosc_name"
                    placeholder="Nazwa sprawności"
                    value={form.sprawnosc_name}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />

                <button
                    type="submit"
                    className="w-full bg-blue-600 text-white py-2 rounded"
                >
                    Dodaj sprawność
                </button>
            </form>
        </div>
    );
}
