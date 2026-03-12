'use client'

import { useState } from "react";
import useGlobalContext from "@/app/helpers/useGlobalContext";

export default function CreateEventPage() {
    const { router,token } = useGlobalContext();
    const [pdf,setPdf] = useState<Blob|undefined>(undefined);
    const [form, setForm] = useState({
        nazwa: "",
        dataWyjazdu: "",
        dataZakonczenia: "",
        opis: "",
        organizatorId: "",
    });

    const [error, setError] = useState<string | null>(null);

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
        setError(null); // czyścimy błąd przy zmianie
    };

    const submit = async (e: React.FormEvent) => {
        e.preventDefault();

        const start = new Date(form.dataWyjazdu);
        const end = new Date(form.dataZakonczenia);

        if (end < start) {
            setError("Data zakończenia nie może być wcześniejsza niż data wyjazdu");
            return;
        }

        await fetch(`${process.env.NEXT_PUBLIC_EVENTS_URI}`, {
            method: "POST",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify(form),
        });

        router.push("/events");
    };

    const withPdf = async (e: React.FormEvent) => {
        e.preventDefault();
        if(!form.opis||!form.nazwa||!form.dataWyjazdu||!form.dataZakonczenia||!form.organizatorId)
            return;
        const start = new Date(form.dataWyjazdu);
        const end = new Date(form.dataZakonczenia);
        if (end < start) {
            setError("Data zakończenia nie może być wcześniejsza niż data wyjazdu");
            return;
        }

        await fetch(`${process.env.NEXT_PUBLIC_EVENTS_URI}/pdf`, {
            method: "POST",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify(form),
        }).then(res=>res.blob())
            .then(res=> {
                const url = window.URL.createObjectURL(res);
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'wydarzenia.pdf'); // nazwa pliku
                document.body.appendChild(link);
                link.click();
                link.remove();
                setPdf(res)
            })
            .catch(console.error);

    };


    return (
        <div className="flex justify-center items-center min-h-screen min-w-screen">
            <button
                onClick={() => router.push("/events")}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full my-1 absolute top-0"
            >
                return
            </button>
            <form
                onSubmit={submit}
                className="max-w-xl border rounded p-6 space-y-4"
            >
                <h2 className="text-xl font-bold">Dodaj wydarzenie</h2>

                <input
                    name="nazwa"
                    placeholder="Nazwa wydarzenia"
                    value={form.nazwa}
                    onChange={handleChange}
                    required
                    className="w-full border px-2 py-1 rounded"
                />

                <input
                    type="datetime-local"
                    name="dataWyjazdu"
                    value={form.dataWyjazdu}
                    onChange={handleChange}
                    required
                    className="w-full border px-2 py-1 rounded"
                />

                <input
                    type="datetime-local"
                    name="dataZakonczenia"
                    value={form.dataZakonczenia}
                    onChange={handleChange}
                    required
                    className="w-full border px-2 py-1 rounded"
                />

                {error && (
                    <p className="text-red-600 text-sm font-medium">
                        {error}
                    </p>
                )}

                <textarea
                    name="opis"
                    placeholder="Opis"
                    value={form.opis}
                    onChange={handleChange}
                    className="w-full border px-2 py-1 rounded"
                />

                <input
                    name="organizatorId"
                    placeholder="ID organizatora"
                    value={form.organizatorId}
                    onChange={handleChange}
                    required
                    className="w-full border px-2 py-1 rounded"
                />

                <div className="flex gap-2">
                    <button
                        type="submit"
                        className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
                    >
                        Zapisz
                    </button>
                    <button
                        type="button"
                        onClick={withPdf}
                        className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
                    >
                        Create with pdf
                    </button>
                    <button
                        type="button"
                        onClick={() => router.push("/events")}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
                    >
                        Anuluj
                    </button>
                </div>
            </form>
            {/*{pdf &&<img src={window.URL.createObjectURL(pdf)*/}
            {/*} alt={"cat"}/>}*/}
        </div>
    );
}
