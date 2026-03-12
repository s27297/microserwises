// helpers/typy/User.ts
export type Sprawnosc = {
    source_id: number;
    sprawnosc_type: string;
    sprawnosc_name: string;
};

export type Payment = {
    source_id: number;
    nazwa: string;
    date: string;
    quantity: number;
};

export type User = {
    id: string;
    name: string;
    surname: string;
    login: string;
    dateOfBirth: string;
    dateOfJoining: string;
    email: string;
    numerTelefonu: string;
    type: string;
    sprawnosci: Sprawnosc[];
    payments: Payment[];
};

// ImageDto
export type Image = {
    source_id: number;
    nazwa: string;
    url: string;
};


export type Like = {
    source_id: number;
    user_id: string;
    value: number;
};

export type Comment = {
    source_id: number;
    user_id: string;
    text: string;
    images: Image[];
    reply?: number;
};



export type Post = {
    id: string;
    nazwa: string;
    opis: string;
    date: string; // w TS używamy string, konwertujesz do Date jeśli trzeba
    likes: Like[];
    comments: Comment[];
    images: Image[];
};

export type Wydarzenie = {
    id: string;
    nazwa: string;
    dataWyjazdu: string;      // LocalDateTime → string
    dataZakonczenia: string;  // LocalDateTime → string
    opis: string;
    organizatorId: string;
};