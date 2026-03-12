'use client'

import React, { useState } from "react";
import { Like, Post } from "@/app/helpers/typy/typy";
import { FaTrash, FaPencil, FaCheck, FaXmark } from "react-icons/fa6";
import {ParamValue} from "next/dist/server/request/params";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    like: Post['likes'][number];
    postId: ParamValue;
    setPostAction: React.Dispatch<React.SetStateAction<Post | null>>;
};

export default function LikeInfo({ like, postId, setPostAction }: Props) {
    const [edit, setEdit] = useState(false);
    const [form, setForm] = useState({ value: like.value });
    const {token}=useGlobalContext()
    const deleteLike = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/likes/${postId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify({ user_id: like.user_id, value: 0}),
        });
        setPostAction(prev => prev && ({
            ...prev,
            likes: prev.likes.filter(l => l.source_id !== like.source_id)
        }));
    };

    const saveLike = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/likes/${postId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify({ user_id: like.user_id, value: form.value }),
        });
        setPostAction(prev => prev && ({
            ...prev,
            likes: prev.likes.map(l =>
                l.source_id === like.source_id ? { ...l, value: form.value } : l
            )
        }));
        setEdit(false);
    };

    return (
        <tr>
            <td className="border px-2 py-1">{like.source_id}</td>
            <td className="border px-2 py-1">{like.user_id}</td>
            <td className="border px-2 py-1">
                {edit ? (
                    <input
                        type="number"
                        value={form.value}
                        onChange={e => setForm({ value: Number(e.target.value) })}
                        className="border px-1 py-0.5 w-20"
                    />
                ) : like.value}
            </td>
            <td className="border px-2 py-1 ">
                <div className={"flex gap-2"}>
                    {edit ? (
                        <>
                            <FaCheck className="text-green-600 cursor-pointer" onClick={saveLike} />
                            <FaXmark className="text-red-600 cursor-pointer" onClick={() => setEdit(false)} />
                        </>
                    ) : (
                        <>
                            <FaPencil className="text-blue-600 cursor-pointer" onClick={() => setEdit(true)} />
                            <FaTrash className="text-red-600 cursor-pointer" onClick={deleteLike} />
                        </>
                    )}
                </div>
            </td>
        </tr>
    );
}
