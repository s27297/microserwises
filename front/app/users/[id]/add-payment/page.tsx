'use client'

import { useParams, useRouter } from "next/navigation";
import React, { useState } from "react";
import useGlobalContext from "@/app/helpers/useGlobalContext";

export default function AddPaymentPage() {
    const { id } = useParams();
    const {router,token} = useGlobalContext();

    const [form, setForm] = useState({
        nazwa: "",
        date: "",
        quantity: "",
        source_id:""
    });

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement>
    ) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            await fetch(`${process.env.NEXT_PUBLIC_USERS_URI}/payment/${id}`, {
                method: "POST",
                headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
                body: JSON.stringify({
                    ...form,
                    quantity: Number(form.quantity),
                }),
            });

            router.push(`/users/${id}`);
        } catch (err) {
            console.log(err);
        }
    };

    return (
        <div className="max-w-md mx-auto p-6">
            <h1 className="text-xl font-bold mb-4">Dodaj wpłatę</h1>

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
                    name="nazwa"
                    placeholder="Nazwa wpłaty"
                    value={form.nazwa}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />

                <input
                    type="date"
                    name="date"
                    value={form.date}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />

                <input
                    type="number"
                    name="quantity"
                    placeholder="Kwota"
                    value={form.quantity}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />

                <button
                    type="submit"
                    className="w-full bg-green-600 text-white py-2 rounded"
                >
                    Dodaj wpłatę
                </button>
            </form>
        </div>
    );
}
