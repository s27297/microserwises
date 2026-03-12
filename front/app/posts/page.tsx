'use client'

import useGlobalContext from "@/app/helpers/useGlobalContext";
import { useEffect, useState } from "react";
import { Post } from "@/app/helpers/typy/typy";
import PostInfo from "./PostInfo";

export default function PostsPage() {
    const { router,token } = useGlobalContext();
    const [posts, setPosts] = useState<Post[]>([]);

    useEffect(() => {
        if(!token)return
        const getPosts = async () => {
            fetch(`${process.env.NEXT_PUBLIC_POSTS_URI}`, { method: "GET",
                headers:{Authorization: `Bearer ${token}`}})
                .then(res => res.json())
                .then(res => {
                    console.log('next')
                    console.log(res)
                    if (Array.isArray(res)) setPosts(res);
                })
                .catch(err => console.log(err));
        };
        getPosts();
    }, [token]);

    if (!posts || posts.length === 0)
        return (
            <div>
                <button
                    onClick={() => router.push("/")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
                >
                    return
                </button>
                <button
                    onClick={() => router.push("/posts/create-post")}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
                >
                    utwórz post
                </button>
                <p>Brak postów</p>
            </div>
        );

    return (
        <div>
            <button
                onClick={() => router.push("/")}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >
            return
            </button>
            <button
                onClick={() => router.push("/posts/create-post")}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
            >
                utwórz post
            </button>
            <table className="w-full border-collapse mt-4">
                <thead>
                <tr className="bg-gray-200 text-black">
                    <th className="border px-2 py-1">Id</th>
                    <th className="border px-2 py-1">Nazwa</th>
                    <th className="border px-2 py-1">Opis</th>
                    <th className="border px-2 py-1">Data</th>
                    <th className="border px-2 py-1 w-12">Actions</th>
                </tr>
                </thead>
                <tbody>
                {posts.map(post => (
                    <PostInfo key={post.id} post={post} setPostsAction={setPosts} />
                ))}
                </tbody>
            </table>
        </div>
    );
}
