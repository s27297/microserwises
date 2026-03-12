'use client'

import { useState } from "react";
import { useRouter } from "next/navigation";
import useGlobalContext from "@/app/helpers/useGlobalContext";

export default function CreatePost() {
    const { router,token } = useGlobalContext();
    const [form, setForm] = useState({
        nazwa: "",
        opis: "",
    });

    const [loading, setLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            console.log(token)
            const res = await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}`, {
                method: "POST",
                headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
                body: JSON.stringify(form),
            });

            if (!res.ok) throw new Error("Nie udało się utworzyć posta");

            router.push("/posts"); // po utworzeniu wracamy do listy
        } catch (err) {
            console.log(err);
            alert("Błąd przy tworzeniu posta");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-xl mx-auto p-6 border rounded shadow-md space-y-4">
            <h2 className="text-xl font-bold mb-2">Utwórz nowy post</h2>
            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                <div className="flex flex-col">
                    <label htmlFor="nazwa" className="mb-1 font-medium">Nazwa</label>
                    <input
                        type="text"
                        name="nazwa"
                        value={form.nazwa}
                        onChange={handleChange}
                        required
                        className="border px-2 py-1 rounded"
                        placeholder="Wpisz nazwę posta"
                    />
                </div>

                <div className="flex flex-col">
                    <label htmlFor="opis" className="mb-1 font-medium">Opis</label>
                    <textarea
                        name="opis"
                        value={form.opis}
                        onChange={handleChange}
                        required
                        className="border px-2 py-1 rounded"
                        placeholder="Wpisz opis posta"
                        rows={4}
                    />
                </div>

                <div className="flex gap-2">
                    <button
                        type="submit"
                        disabled={loading}
                        className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
                    >
                        {loading ? "Tworzenie..." : "Utwórz"}
                    </button>
                    <button
                        type="button"
                        onClick={() => router.push("/posts")}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
                    >
                        Anuluj
                    </button>
                </div>
            </form>
        </div>
    );
}
