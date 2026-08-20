export default function Navbar({ theme, toggleTheme }) {
  return (
    <nav className="flex justify-between items-end border-b border-[#d8d0c5] dark:border-[#3a3030] pb-6 mb-8 transition-colors duration-500">
      <div>
        <h1 className="text-4xl md:text-5xl font-extrabold tracking-[-0.06em] leading-none mb-2 text-[#241f1c] dark:text-[#f1e9de]">
          NOTES.
        </h1>

        <p className="text-[10px] font-bold tracking-[0.25em] uppercase text-[#681f2a] dark:text-[#c77a86]">
          Editorial Archive
        </p>
      </div>

      <button
        onClick={toggleTheme}
        className="text-[10px] font-bold uppercase tracking-[0.2em] text-[#766d65] dark:text-[#a99c91] hover:text-[#681f2a] dark:hover:text-[#c77a86] transition-colors duration-300 cursor-pointer"
      >
        {theme === 'light' ? 'Dark' : 'Light'}
      </button>
    </nav>
  );
}