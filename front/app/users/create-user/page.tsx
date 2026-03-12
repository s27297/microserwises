'use client'

import { useState } from "react";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type UserForm = {
    name: string;
    surname: string;
    login: string;
    dateOfBirth: string; // używamy string dla input[type="date"]
    email: string;
    numerTelefonu: string;
    type: string;
};

export default function CreateUser() {
    const { router,token } = useGlobalContext();

    const [form, setForm] = useState<UserForm>({
        name: "",
        surname: "",
        login: "",
        dateOfBirth: "",
        email: "",
        numerTelefonu: "",
        type: ""
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            console.log(process.env.NEXT_PUBLIC_USERS_URI)
            const res = await fetch(`${process.env.NEXT_PUBLIC_USERS_URI}`, {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
                body: JSON.stringify(form)
            });

            if (!res.ok) throw new Error("Błąd podczas dodawania użytkownika");

            alert("Użytkownik dodany pomyślnie!");
            router.push("/users"); // przekierowanie na listę użytkowników
        } catch (err: any) {
            setError(err.message || "Nieznany błąd");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-md mx-auto mt-8 p-4 border rounded shadow">
            <h2 className="text-xl font-bold mb-4">Dodaj użytkownika</h2>

            {error && <div className="text-red-600 mb-2">{error}</div>}

            <form onSubmit={handleSubmit} className="space-y-2">
                <input
                    type="text"
                    name="name"
                    placeholder="Name"
                    value={form.name}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />
                <input
                    type="text"
                    name="surname"
                    placeholder="Surname"
                    value={form.surname}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />
                <input
                    type="text"
                    name="login"
                    placeholder="Login"
                    value={form.login}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />
                <input
                    type="date"
                    name="dateOfBirth"
                    placeholder="Date of Birth"
                    value={form.dateOfBirth}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={form.email}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />
                <input
                    type="text"
                    name="numerTelefonu"
                    placeholder="Numer telefonu"
                    value={form.numerTelefonu}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                />
                <select
                    name="type"
                    value={form.type}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                    required
                >
                    <option value="" className={"text-black"}>Wybierz typ</option>
                    <option value="Druzynowy" className={"text-black"}>Drużynowy</option>
                    <option value="Zuch" className={"text-black"}>Zuch</option>
                    <option value="Przyboczny" className={"text-black"}>Przyboczny</option>
                    <option value="Rodzic" className={"text-black"}>Rodzic</option>
                </select>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    {loading ? "Dodawanie..." : "Dodaj użytkownika"}
                </button>

                <button
                    onClick={()=>router.push("/users")}
                    className="w-full bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                >
                    Anuluj
                </button>
            </form>
        </div>
    );
}
