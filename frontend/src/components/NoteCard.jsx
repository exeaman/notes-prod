export default function NoteCard({ note, openModal, handleDelete, index }) {
  return (
    <div
      style={{ animationDelay: `${index * 45}ms` }}
className="note-enter group border border-[#d8d0c5] dark:border-[#3a3030] p-6 bg-[#faf7f1] dark:bg-[#211a1b] flex flex-col justify-between min-h-[250px] transition-all duration-300 hover:translate-x-[-3px] hover:translate-y-[-3px] hover:shadow-[6px_6px_0_0_#681f2a] dark:hover:shadow-[6px_6px_0_0_#9a4655]"    >
      <div>
        <div className="flex justify-between items-start mb-4 gap-4">
          <h3 className="text-xl font-bold tracking-tight leading-snug text-[#241f1c] dark:text-[#f1e9de]">
            {note.title}
          </h3>

          <span className="shrink-0 text-[10px] font-bold uppercase tracking-widest bg-[#e8ded2] dark:bg-[#342628] text-[#681f2a] dark:text-[#c77a86] px-2 py-1">
            {new Date(note.createdAt).toLocaleDateString()}
          </span>
        </div>

        <p className="text-sm leading-relaxed mb-6 text-[#766d65] dark:text-[#a99c91] line-clamp-4">
          {note.content}
        </p>
      </div>

      <div className="flex justify-between items-center border-t border-[#e8e0d6] dark:border-[#342b2c] pt-4 mt-auto">
        <span className="text-xs font-bold uppercase tracking-widest text-[#681f2a] dark:text-[#c77a86]">
          By {note.author}
        </span>

        <div className="flex gap-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
          <button
            onClick={() => openModal(note)}
            className="text-xs font-bold uppercase tracking-wider text-[#766d65] dark:text-[#a99c91] hover:text-[#681f2a] dark:hover:text-[#c77a86] transition-colors duration-300 cursor-pointer"
          >
            Edit
          </button>

          <button
            onClick={() => handleDelete(note.id)}
            className="text-xs font-bold uppercase tracking-wider text-[#766d65] dark:text-[#a99c91] hover:text-[#9b3030] transition-colors duration-300 cursor-pointer"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}