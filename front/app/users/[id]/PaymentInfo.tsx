'use client'

import React, { Dispatch, SetStateAction, useState } from "react";
import { FaCheck, FaTrash } from "react-icons/fa";
import { FaPencil, FaXmark } from "react-icons/fa6";
import { Payment, User } from "@/app/helpers/typy/typy";
import {ParamValue} from "next/dist/server/request/params";
import useGlobalContext from "@/app/helpers/useGlobalContext";

type Props = {
    payment: Payment;
    setUserAction: Dispatch<SetStateAction<User | null>>;
    id: ParamValue;
};

export default function PaymentInfo({ payment, setUserAction, id }: Props) {
    const [editPayment, setEditPayment] = useState(false);
const {token}=useGlobalContext()
    const [paymentForm, setPaymentForm] = useState({
        nazwa: payment.nazwa,
        date: payment.date.slice(0, 10), // yyyy-mm-dd for input[type=date]
        quantity: payment.quantity.toString(),
    });

    // 🗑️ DELETE
    const deletePayment = async () => {
        await fetch(
            `${process.env.NEXT_PUBLIC_USERS_URI}/payment?id=${id}&source_id=${payment.source_id}`,
            { method: "DELETE", headers:{ Authorization: `Bearer ${token}`} },

        );

        setUserAction(prev =>
            prev
                ? {
                    ...prev,
                    payments: prev.payments.filter(
                        p => p.source_id !== payment.source_id
                    ),
                }
                : prev
        );
    };

    // ✏️ SAVE
    const savePayment = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_USERS_URI}/payment/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                source_id: payment.source_id,
                nazwa: paymentForm.nazwa,
                date: paymentForm.date,
                quantity: Number(paymentForm.quantity),
            }),
        });

        setUserAction(prev =>
            prev
                ? {
                    ...prev,
                    payments: prev.payments.map(p =>
                        p.source_id === payment.source_id
                            ? {
                                ...p,
                                nazwa: paymentForm.nazwa,
                                date: paymentForm.date,
                                quantity: Number(paymentForm.quantity),
                            }
                            : p
                    ),
                }
                : prev
        );

        setEditPayment(false);
    };

    return (
        <tr>
            <td className="border px-2 py-1">{payment.source_id}</td>

            <td className="border px-2 py-1">
                {editPayment ? (
                    <input
                        value={paymentForm.nazwa}
                        onChange={e =>
                            setPaymentForm(f => ({
                                ...f,
                                nazwa: e.target.value,
                            }))
                        }
                        className="border px-1 w-full"
                    />
                ) : (
                    payment.nazwa
                )}
            </td>

            <td className="border px-2 py-1">
                {editPayment ? (
                    <input
                        type="date"
                        value={paymentForm.date}
                        onChange={e =>
                            setPaymentForm(f => ({
                                ...f,
                                date: e.target.value,
                            }))
                        }
                        className="border px-1 w-full"
                    />
                ) : (
                    new Date(payment.date).toLocaleDateString()
                )}
            </td>

            <td className="border px-2 py-1">
                {editPayment ? (
                    <input
                        type="number"
                        step="0.01"
                        value={paymentForm.quantity}
                        onChange={e =>
                            setPaymentForm(f => ({
                                ...f,
                                quantity: e.target.value,
                            }))
                        }
                        className="border px-1 w-full"
                    />
                ) : (
                    `${payment.quantity} zł`
                )}
            </td>

            <td className="border px-2 py-1">
                <div className="flex gap-3 ">
                    {editPayment ? (
                        <>
                            <FaCheck
                                className="text-green-600 cursor-pointer"
                                onClick={savePayment}
                            />
                            <FaXmark
                                className="text-red-600 cursor-pointer"
                                onClick={() => setEditPayment(false)}
                            />
                        </>
                    ) : (
                        <>
                            <FaPencil
                                className="text-blue-600 cursor-pointer"
                                onClick={() => setEditPayment(true)}
                            />
                            <FaTrash
                                className="text-red-600 cursor-pointer"
                                onClick={deletePayment}
                            />
                        </>
                    )}
                </div>
            </td>
        </tr>
    );
}
