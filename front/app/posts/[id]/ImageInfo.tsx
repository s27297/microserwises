'use client'

import React, { useState } from "react";
import { Image, Post } from "@/app/helpers/typy/typy";
import { FaTrash, FaPencil, FaCheck, FaXmark } from "react-icons/fa6";
import {ParamValue} from "next/dist/server/request/params";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    image: Image;
    postId: ParamValue;
    setPostAction: React.Dispatch<React.SetStateAction<Post | null>>;
};

export default function ImageInfo({ image, postId, setPostAction }: Props) {
    const [edit, setEdit] = useState(false);
    const [form, setForm] = useState({ nazwa: image.nazwa, url: image.url });
    const {token}=useGlobalContext()
    const deleteImage = async () => {
        console.log(JSON.stringify({id:postId,source_id:image.source_id}));
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/image`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body:JSON.stringify({id:postId,source_id:image.source_id}),
        });
        setPostAction(prev => prev && ({
            ...prev,
            images: prev.images.filter(img => img.source_id !== image.source_id)
        }));
    };

    const saveImage = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/image/${postId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify({ source_id: image.source_id, ...form }),
        });
        setPostAction(prev => prev && ({
            ...prev,
            images: prev.images.map(img =>
                img.source_id === image.source_id ? { ...img, ...form } : img
            )
        }));
        setEdit(false);
    };

    return (
        <tr>
            <td className="border px-2 py-1">{image.source_id}</td>
            <td className="border px-2 py-1">
                {edit ? (
                    <input
                        value={form.nazwa}
                        onChange={e => setForm(prev => ({ ...prev, nazwa: e.target.value }))}
                        className="border px-1 py-0.5 w-full"
                    />
                ) : image.nazwa}
            </td>
            <td className="border px-2 py-1">
                {edit ? (
                    <input
                        value={form.url}
                        onChange={e => setForm(prev => ({ ...prev, url: e.target.value }))}
                        className="border px-1 py-0.5 w-full"
                    />
                ) : image.url}
            </td>
            <td className="border px-2 py-1 ">
                <div className={"flex gap-2"}>
                    {edit ? (
                        <>
                            <FaCheck className="text-green-600 cursor-pointer" onClick={saveImage}/>
                            <FaXmark className="text-red-600 cursor-pointer" onClick={() => setEdit(false)}/>
                        </>
                    ) : (
                        <>
                            <FaPencil className="text-blue-600 cursor-pointer" onClick={() => setEdit(true)}/>
                            <FaTrash className="text-red-600 cursor-pointer" onClick={deleteImage}/>
                        </>
                    )}
                </div>
            </td>
        </tr>
);
}
