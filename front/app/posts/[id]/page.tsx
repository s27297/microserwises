'use client'

import React, { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import useGlobalContext from "@/app/helpers/useGlobalContext";
import { Post } from "@/app/helpers/typy/typy";
import { FaPlus, FaTrash, FaPencil, FaCheck, FaXmark } from "react-icons/fa6";
import ImageInfo from "@/app/posts/[id]/ImageInfo";
import LikeInfo from "@/app/posts/[id]/LikeInfo";
import CommentInfo from "@/app/posts/[id]/CommentInfo";

export default function PostDetailsPage() {
    const { router,token } = useGlobalContext();
    const { id } = useParams();
    const [post, setPost] = useState<Post | null>(null);
    const [loading, setLoading] = useState(true);
    console.log(post)
    useEffect(() => {
        if(!token)return;
        const fetchPost = async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}/full/${id}`,{
                    headers:{Authorization: `Bearer ${token}`}
                });
                const data = await res.json();
                setPost(data);
            } catch (err) {
                console.log(err);
            } finally {
                setLoading(false);
            }
        };
        fetchPost();
    }, [id,token]);

    if (loading) return <div>Ładowanie...</div>;
    if (!post)
        return (
            <div>
                <button
                    onClick={() => router.push("/posts")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
                >
                    return
                </button>
                <p>Nie znaleziono posta</p>
            </div>
        );

    return (
        <div className="max-w-4xl mx-auto p-6 space-y-6">
            <button
                onClick={() => router.push("/posts")}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >
                return
            </button>

            {/* BASIC INFO */}
            <section className="border rounded p-4">
                <h2 className="text-xl font-bold mb-2">Szczegóły posta</h2>
                <div className="grid grid-cols-1 gap-2">
                    <div><b>Nazwa:</b> {post.nazwa}</div>
                    <div><b>Opis:</b> {post.opis}</div>
                    <div><b>Data:</b> {new Date(post.date).toLocaleDateString()}</div>
                </div>
            </section>

            {/* IMAGES */}
            <section className="border rounded p-4">
                <div className="flex flex-row gap-2 mb-2">
                    <h2 className="text-xl font-bold">Obrazy</h2>
                    <button onClick={()=>router.push(`/posts/${id}/create-image`)}>
                        <FaPlus className="text-green-600" />
                    </button>
                </div>
                {post.images?.length > 0 ? (
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-200 text-black">
                            <th className="border px-2 py-1">ID</th>
                            <th className="border px-2 py-1">Nazwa</th>
                            <th className="border px-2 py-1">URL</th>
                            <th className="border px-2 py-1 w-12">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {post.images.map(img => (
                            <ImageInfo image={img} postId={id} setPostAction={setPost} key={img.source_id}/>
                        ))}
                        </tbody>
                    </table>
                ):(
                    <div>Brak obrazów</div>
                ) }
            </section>

            {/* LIKES */}
            <section className="border rounded p-4">
                <div className={"flex flex-row gap-2 mb-2"}>
                    <h2 className="text-xl font-bold mb-2">Polubienia</h2>
                    <button onClick={() => router.push(`/posts/${id}/create-like`)}>
                        <FaPlus className="text-green-600"/>
                    </button>
                </div>
                {post.likes?.length > 0 ? (
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-200 text-black">
                            <th className="border px-2 py-1">ID</th>
                            <th className="border px-2 py-1">User ID</th>
                            <th className="border px-2 py-1">Wartość</th>
                            <th className="border px-2 py-1">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {post.likes.map(like => (
                            <LikeInfo like={like} postId={id} setPostAction={setPost} key={like.source_id}/>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <div>Brak polubień</div>
                )}
            </section>

            {/* COMMENTS */}
            <section className="border rounded p-4">
                <div className="flex flex-row gap-2 mb-2">
                    <h2 className="text-xl font-bold">Komentarze</h2>
                    <button onClick={() => router.push(`/posts/${id}/create-comment`)}>
                        <FaPlus className="text-green-600"/>
                    </button>
                </div>
                {post.comments?.length > 0 ? (
                    <table className="w-full border-collapse">
                        <thead>
                        <tr className="bg-gray-200 text-black">
                            <th className="border px-2 py-1">ID</th>
                            <th className="border px-2 py-1">User ID</th>
                            <th className="border px-2 py-1">Tekst</th>
                            <th className="border px-2 py-1">Odpowiedź</th>
                            <th className="border px-2 py-1 w-12">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {post.comments.map(c => (
                            <CommentInfo comment={c} postId={id} setPostAction={setPost} key={c.source_id}/>
                        )) }
                        </tbody>
                    </table>
                ):(
                    <div>Brak komentarzy</div>
                )}
            </section>
        </div>
    );
}
