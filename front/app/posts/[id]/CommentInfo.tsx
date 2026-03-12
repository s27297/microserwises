'use client'

import React, { useState } from "react";
import { Comment, Post } from "@/app/helpers/typy/typy";
import { FaTrash, FaPencil, FaCheck, FaXmark } from "react-icons/fa6";
import {ParamValue} from "next/dist/server/request/params";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    comment: Comment;
    postId: ParamValue;
    setPostAction: React.Dispatch<React.SetStateAction<Post | null>>;
};

export default function CommentInfo({ comment, postId, setPostAction }: Props) {
    const [edit, setEdit] = useState(false);
    const [form, setForm] = useState({ text: comment.text, reply: comment.reply });
    const {token}=useGlobalContext()
    const deleteComment = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/comment`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body:JSON.stringify({id:postId,source_id:comment.source_id}),
        });
        setPostAction(prev => prev && ({
            ...prev,
            comments: prev.comments.filter(c => c.source_id !== comment.source_id)
        }));
    };

    const saveComment = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/comment/${postId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify({ source_id: comment.source_id, ...form }),
        });
        setPostAction(prev => prev && ({
            ...prev,
            comments: prev.comments.map(c =>
                c.source_id === comment.source_id ? { ...c, ...form } : c
            )
        }));
        setEdit(false);
    };

    return (
        <tr>
            <td className="border px-2 py-1">{comment.source_id}</td>
            <td className="border px-2 py-1">{comment.user_id}</td>
            <td className="border px-2 py-1">
                {edit ? (
                    <input
                        value={form.text}
                        onChange={e => setForm(prev => ({ ...prev, text: e.target.value }))}
                        className="border px-1 py-0.5 w-full"
                    />
                ) : comment.text}
            </td>
            <td className="border px-2 py-1">
                {edit ? (
                    <input
                        type={"number"}
                        value={form.reply}
                        onChange={e => setForm(prev => ({ ...prev, reply:  parseInt(e.target.value) }))}
                        className="border px-1 py-0.5 w-full"
                    />
                ) : comment.reply || "-"}
            </td>
            <td className="border px-2 py-1 ">
                <div className={"flex gap-2"}>
                    {edit ? (
                        <>
                            <FaCheck className="text-green-600 cursor-pointer" onClick={saveComment}/>
                            <FaXmark className="text-red-600 cursor-pointer" onClick={() => setEdit(false)}/>
                        </>
                    ) : (
                        <>
                            <FaPencil className="text-blue-600 cursor-pointer" onClick={() => setEdit(true)}/>
                            <FaTrash className="text-red-600 cursor-pointer" onClick={deleteComment}/>
                        </>
                    )}
                </div>
            </td>
        </tr>
);
}
