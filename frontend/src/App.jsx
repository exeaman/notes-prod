import { useState, useEffect } from 'react';
import axios from 'axios';
import Navbar from './components/Navbar';
import LeftPanel from './components/LeftPanel';
import NoteCard from './components/NoteCard';

export default function App() {
    const [notes, setNotes] = useState([]);
    const [deleteId, setDeleteId] = useState(null);
    const [searchParams, setSearchParams] = useState({ type: 'ALL', keyword: '', start: '', end: '' });
    const [theme, setTheme] = useState(localStorage.getItem('theme') || 'light');

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingId, setEditingId] = useState(null);
    const [formData, setFormData] = useState({ title: '', content: '', author: 'Editor' });

    const apiUrl = import.meta.env.VITE_API_BASE_URL;

    // Theme Switcher Effect
    useEffect(() => {
        if (theme === 'dark') {
            document.documentElement.classList.add('dark');
        } else {
            document.documentElement.classList.remove('dark');
        }
        localStorage.setItem('theme', theme);
    }, [theme]);

    const toggleTheme = () => setTheme(theme === 'light' ? 'dark' : 'light');

    // Fetch Logic
    useEffect(() => {
        fetchNotes();
    }, [searchParams]);

    const fetchNotes = async () => {
        try {
            let url = apiUrl;
            const { type, keyword, start, end } = searchParams;
            if (type === 'TITLE' && keyword) url = `${apiUrl}/search/title?keyword=${keyword}`;
            if (type === 'CONTENT' && keyword) url = `${apiUrl}/search/content?keyword=${keyword}`;
            if (type === 'AUTHOR' && keyword) url = `${apiUrl}/author/${keyword}`;
            if (type === 'DATE' && start && end) url = `${apiUrl}/search/date?startDate=${start}&endDate=${end}`;

            const response = await axios.get(url);
            setNotes(Array.isArray(response.data) ? response.data : [response.data]);
        } catch (error) {
            setNotes([]);
        }
    };

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (editingId) await axios.put(`${apiUrl}/${editingId}`, formData);
            else await axios.post(apiUrl, formData);
            setIsModalOpen(false);
            fetchNotes();
        } catch (error) {
            console.error("Error saving note:", error);
        }
    };

    const handleDelete = async () => {
        if (!deleteId) return;

        try {
            await axios.delete(`${apiUrl}/${deleteId}`);
            setDeleteId(null);
            fetchNotes();
        } catch (error) {
            console.error("Error deleting note:", error);
        }
    };

    const openModal = (note = null) => {
        setEditingId(note ? note.id : null);
        setFormData(note ? { title: note.title, content: note.content, author: note.author } : { title: '', content: '', author: 'Editor' });
        setIsModalOpen(true);
    };

    return (
        <div className="min-h-screen bg-[#F4EFE6] dark:bg-[#171313] text-[#241F1C] dark:text-[#F1E9DE] transition-colors duration-500">
            <div className="max-w-7xl mx-auto p-6 md:p-12">

                <Navbar theme={theme} toggleTheme={toggleTheme} />

                <div className="grid grid-cols-1 md:grid-cols-12 gap-12">
                    {/* Left Panel */}
                    <div className="md:col-span-3">
                        <LeftPanel searchParams={searchParams} setSearchParams={setSearchParams} openModal={openModal} />
                    </div>

                    {/* Right Panel: Grid */}
                    <div className="md:col-span-9">
                        <div className="flex justify-between items-end mb-6">
                            <h2 className="text-lg font-bold tracking-tight text-[#241f1c] dark:text-[#f1e9de]">
                                INDEX
                            </h2>

                            <span className="text-[10px] font-bold uppercase tracking-[0.2em] text-[#766d65] dark:text-[#a99c91]">
                                [{notes.length} Records]
                            </span>
                        </div>

                        {notes.length === 0 ? (
                            <p className="text-[#766d65] dark:text-[#a99c91] text-sm font-medium">
                                No records found matching criteria.
                            </p>) : (
                            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                                {notes.map((note, index) => (
                                    <NoteCard
                                        key={note.id}
                                        note={note}
                                        index={index}
                                        openModal={openModal}
                                        handleDelete={(id) => setDeleteId(id)}
                                    />
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            </div>

            {/* Modal */}
            {isModalOpen && (
                <div className="fixed inset-0 bg-[#241f1c]/60 dark:bg-black/80 flex items-center justify-center p-4 z-50 backdrop-blur-sm">
                    <div className="bg-[#faf7f1] dark:bg-[#211a1b] border border-[#d8d0c5] dark:border-[#3a3030] w-full max-w-2xl p-8 md:p-12 shadow-2xl">

                        <div className="flex justify-between items-center mb-8 pb-4 border-b border-[#d8d0c5] dark:border-[#3a3030]">
                            <div>
                                <p className="text-[10px] font-bold uppercase tracking-[0.2em] text-[#681f2a] dark:text-[#c77a86] mb-2">
                                    Editorial Archive
                                </p>

                                <h2 className="text-2xl font-bold tracking-tight text-[#241f1c] dark:text-[#f1e9de]">
                                    {editingId ? 'EDIT ENTRY.' : 'NEW ENTRY.'}
                                </h2>
                            </div>

                            <button
                                onClick={() => setIsModalOpen(false)}
                                className="text-xs font-bold uppercase tracking-widest text-[#766d65] dark:text-[#a99c91] hover:text-[#681f2a] dark:hover:text-[#c77a86] transition-colors duration-300 cursor-pointer"
                            >
                                Close ✕
                            </button>
                        </div>

                        <form onSubmit={handleSave} className="flex flex-col gap-6">

                            <input
                                type="text"
                                placeholder="TITLE"
                                className="w-full bg-transparent border-b border-[#d8d0c5] dark:border-[#3a3030] py-3 text-2xl font-bold tracking-tight outline-none placeholder-[#b9afa5] dark:placeholder-[#625758] text-[#241f1c] dark:text-[#f1e9de] focus:border-[#681f2a] dark:focus:border-[#c77a86] transition-colors duration-300"
                                value={formData.title}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        title: e.target.value
                                    })
                                }
                                required
                            />

                            <textarea
                                placeholder="Content..."
                                className="w-full bg-[#f4efe6] dark:bg-[#171313] border border-[#d8d0c5] dark:border-[#3a3030] p-4 text-sm leading-relaxed outline-none min-h-[200px] resize-y text-[#241f1c] dark:text-[#f1e9de] placeholder-[#a99c91] focus:border-[#681f2a] dark:focus:border-[#c77a86] transition-colors duration-300"
                                value={formData.content}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        content: e.target.value
                                    })
                                }
                                required
                            />

                            <input
                                type="text"
                                placeholder="AUTHOR"
                                className="w-full bg-transparent border-b border-[#d8d0c5] dark:border-[#3a3030] py-2 text-xs font-bold uppercase tracking-widest outline-none text-[#241f1c] dark:text-[#f1e9de] placeholder-[#a99c91] focus:border-[#681f2a] dark:focus:border-[#c77a86] transition-colors duration-300"
                                value={formData.author}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        author: e.target.value
                                    })
                                }
                                required
                            />

                            <button
                                type="submit"
                                className="bg-[#681f2a] dark:bg-[#8e3d4c] text-[#f4efe6] font-bold uppercase tracking-widest py-4 mt-4 hover:bg-[#4e1720] dark:hover:bg-[#a65363] transition-colors duration-300 cursor-pointer"
                            >
                                {editingId ? 'Save Changes' : 'Publish Entry'}
                            </button>

                        </form>
                    </div>
                </div>
            )}
            {deleteId && (
                <div className="fixed inset-0 bg-[#241f1c]/60 dark:bg-black/80 flex items-center justify-center p-4 z-50 backdrop-blur-sm">
                    <div className="bg-[#faf7f1] dark:bg-[#211a1b] border border-[#d8d0c5] dark:border-[#3a3030] w-full max-w-md p-8 shadow-2xl">

                        <p className="text-[10px] font-bold uppercase tracking-[0.2em] text-[#681f2a] dark:text-[#c77a86] mb-3">
                            Delete Entry
                        </p>

                        <h2 className="text-2xl font-bold tracking-tight text-[#241f1c] dark:text-[#f1e9de] mb-3">
                            Permanently remove this note?
                        </h2>

                        <p className="text-sm leading-relaxed text-[#766d65] dark:text-[#a99c91] mb-8">
                            This action cannot be undone.
                        </p>

                        <div className="flex justify-end gap-6">
                            <button
                                onClick={() => setDeleteId(null)}
                                className="text-xs font-bold uppercase tracking-widest text-[#766d65] dark:text-[#a99c91] hover:text-[#241f1c] dark:hover:text-[#f1e9de] transition-colors duration-300 cursor-pointer"
                            >
                                Cancel
                            </button>

                            <button
                                onClick={handleDelete}
                                className="bg-[#681f2a] dark:bg-[#8e3d4c] text-[#f4efe6] text-xs font-bold uppercase tracking-widest px-5 py-3 hover:bg-[#4e1720] dark:hover:bg-[#a65363] transition-colors duration-300 cursor-pointer"
                            >
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}