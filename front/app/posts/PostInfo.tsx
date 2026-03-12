'use client'

import React, { Dispatch, SetStateAction, useState } from "react";
import { FaPencil, FaTrash, FaCheck, FaXmark } from "react-icons/fa6";
import { Post } from "@/app/helpers/typy/typy";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    post: Post;
    setPostsAction: Dispatch<SetStateAction<Post[]>>;
};

export default function PostInfo({ post, setPostsAction }: Props) {
    const [editPost, setEditPost] = useState(false);
   const {router,token} = useGlobalContext();
    const [form, setForm] = useState({
        nazwa: post.nazwa,
        opis: post.opis,
    });
console.log(form)
    const deletePost = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/${post.id}`, { method: "DELETE",
        headers: {Authorization: `Bearer ${token}` }});
        setPostsAction(prev => prev.filter(p => p.id !== post.id));
    };

    const savePost = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/${post.id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}` },
            body: JSON.stringify(form),
        });

        setPostsAction(prev =>
            prev.map(p => (p.id === post.id ? { ...p, ...form } : p))
        );
        setEditPost(false);
    };

    return (
        <tr>
            <td className="border px-2 py-1 cursor-pointer hover:underline hover:text-blue-600"
                onClick={()=>router.push(`/posts/${post.id}`)}>{post.id}</td>
            <td className="border px-2 py-1">
                {editPost ? (
                    <input
                        value={form.nazwa}
                        onChange={e => setForm(f => ({ ...f, nazwa: e.target.value }))}
                        className="border px-1 w-full"
                    />
                ) : (
                    post.nazwa
                )}
            </td>
            <td className="border px-2 py-1">
                {editPost ? (
                    <input
                        value={form.opis}
                        onChange={e => setForm(f => ({ ...f, opis: e.target.value }))}
                        className="border px-1 w-full"
                    />
                ) : (
                    post.opis
                )}
            </td>
            <td className="border px-2 py-1">
                {new Date(post.date).toLocaleDateString()}
            </td>
            <td className="border px-2 py-1 ">
                <div className={"flex gap-2 flex-row"}>
                    {editPost ? (
                        <>
                            <FaCheck
                                className="text-green-600 cursor-pointer"
                                onClick={savePost}
                            />
                            <FaXmark
                                className="text-red-600 cursor-pointer"
                                onClick={() => setEditPost(false)}
                            />
                        </>
                    ) : (
                        <>
                            <FaPencil
                                className="text-blue-600 cursor-pointer"
                                onClick={() => setEditPost(true)}
                            />
                            <FaTrash
                                className="text-red-600 cursor-pointer"
                                onClick={deletePost}
                            />
                        </>
                    )}
                </div>
            </td>
        </tr>
    );
}
