'use client'

import { useState } from "react";
import useGlobalContext from "@/app/helpers/useGlobalContext";
import { useParams } from "next/navigation";

export default function AddLike() {
    const { router,token } = useGlobalContext();
    const { id } = useParams();

    const [form, setForm] = useState({
        user_id: "",
        value: 1,
    });
    const [loading, setLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: name === "value" ? Number(value) : value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/likes/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
                body: JSON.stringify(form),
            });
            if (!res.ok) throw new Error("Nie udało się dodać polubienia");
            router.push(`/posts/${id}`);
        } catch (err) {
            console.log(err);
            alert("Błąd przy dodawaniu polubienia");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-xl mx-auto p-6 border rounded shadow-md space-y-4">
            <h2 className="text-xl font-bold">Dodaj polubienie</h2>
            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                <input
                    type="text"
                    name="user_id"
                    placeholder="ID użytkownika"
                    value={form.user_id}
                    onChange={handleChange}
                    required
                    className="border px-2 py-1 rounded"
                />
                <input
                    type="number"
                    name="value"
                    placeholder="Wartość (np. 1)"
                    value={form.value}
                    onChange={handleChange}
                    required
                    className="border px-2 py-1 rounded"
                />
                <div className="flex gap-2">
                    <button
                        type="submit"
                        disabled={loading}
                        className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
                    >
                        Dodaj
                    </button>
                    <button
                        type="button"
                        onClick={() => router.push(`/posts/${id}`)}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
                    >
                        Anuluj
                    </button>
                </div>
            </form>
        </div>
    );
}
