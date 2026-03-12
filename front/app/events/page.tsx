'use client'

import { useEffect, useState } from "react";
import useGlobalContext from "@/app/helpers/useGlobalContext";
import { Wydarzenie } from "@/app/helpers/typy/typy";
import WydarzenieInfo from "./WydarzenieInfo";

export default function EventsPage() {
    const { router,token } = useGlobalContext();
    const [events, setEvents] = useState<Wydarzenie[]>([]);

    useEffect(() => {
        if(!token)return
        fetch(`${process.env.NEXT_PUBLIC_EVENTS_URI}`,{headers:{Authorization: `Bearer ${token}`}})
            .then(res => res.json())
            .then(data => Array.isArray(data) && setEvents(data))
            .catch(console.log);
    }, [token]);

    return (
        <div>
            <button
                onClick={() => router.push("/")}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full my-1"
            >
                return
            </button>

            <button
                onClick={() => router.push("/events/create-event")}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full my-1"
            >
                Dodaj wydarzenie
            </button>

            {events.length === 0 ? (
                <p>Brak wydarzeń</p>
            ) : (
                <table className="w-full border-collapse mt-4">
                    <thead>
                    <tr className="bg-gray-200 text-black">
                        <th className="border px-2 py-1">ID</th>
                        <th className="border px-2 py-1">Nazwa</th>
                        <th className="border px-2 py-1">Wyjazd</th>
                        <th className="border px-2 py-1">Zakończenie</th>
                        <th className="border px-2 py-1">Opis</th>
                        <th className="border px-2 py-1">Organizator</th>
                        <th className="border px-2 py-1 w-16">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {events.map(e => (
                        <WydarzenieInfo
                            key={e.id}
                            wydarzenie={e}
                            setEvents={setEvents}
                        />
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
