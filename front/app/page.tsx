'use client'
import useGlobalContext from "@/app/helpers/useGlobalContext";

export default function Home() {

  const { router } = useGlobalContext();
  return (
      <div>
          <button onClick={() => router.push("/users")}
                  className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
          >Users
          </button>
          <button onClick={() => router.push("/posts")}
                  className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
          >posts
          </button>
          <button onClick={() => router.push("/events")}
                  className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 w-full p-1 my-1"
          >events
          </button>
      </div>
  );
}
