import { useEffect, useRef, useState } from 'react';

const options = [
  { value: 'ALL', label: 'All Entries' },
  { value: 'TITLE', label: 'Search by Title' },
  { value: 'CONTENT', label: 'Search by Content' },
  { value: 'AUTHOR', label: 'Filter by Author' },
  { value: 'DATE', label: 'Filter by Date Range' }
];

export default function FilterSelect({ value, onChange }) {
  const [open, setOpen] = useState(false);
  const ref = useRef(null);

  const selected = options.find(option => option.value === value);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (ref.current && !ref.current.contains(event.target)) {
        setOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div ref={ref} className="relative">

      <button
        type="button"
        onClick={() => setOpen(!open)}
        className="
          w-full
          flex items-center justify-between
          bg-[#faf7f1] dark:bg-[#211a1b]
          border border-[#d8d0c5] dark:border-[#3a3030]
          py-3 px-4
          text-[11px]
          font-bold
          uppercase
          tracking-[0.12em]
          text-[#241f1c] dark:text-[#f1e9de]
          hover:border-[#681f2a] dark:hover:border-[#9a4655]
          focus:border-[#681f2a] dark:focus:border-[#c77a86]
          outline-none
          transition-colors duration-300
          cursor-pointer
        "
      >
        <span>{selected.label}</span>

        <span
          className={`
            text-[#681f2a] dark:text-[#c77a86]
            text-xs
            transition-transform duration-300
            ${open ? 'rotate-180' : ''}
          `}
        >
          ↓
        </span>
      </button>

      {open && (
        <div
          className="
            absolute
            z-40
            top-full left-0 right-0
            mt-1
            bg-[#faf7f1] dark:bg-[#211a1b]
            border border-[#d8d0c5] dark:border-[#3a3030]
            overflow-hidden
          "
        >
          {options.map((option) => (
            <button
              key={option.value}
              type="button"
              onClick={() => {
                onChange(option.value);
                setOpen(false);
              }}
              className={`
                w-full
                text-left
                px-4 py-3
                text-[10px]
                font-bold
                uppercase
                tracking-[0.12em]
                border-b last:border-b-0
                border-[#e8e0d6] dark:border-[#342b2c]
                transition-colors duration-200
                cursor-pointer

                ${
                  option.value === value
                    ? 'text-[#681f2a] dark:text-[#c77a86] bg-[#f1e9de] dark:bg-[#2a2021]'
                    : 'text-[#766d65] dark:text-[#a99c91] hover:text-[#681f2a] dark:hover:text-[#c77a86] hover:bg-[#f4efe6] dark:hover:bg-[#261d1e]'
                }
              `}
            >
              {option.label}
            </button>
          ))}
        </div>
      )}

    </div>
  );
}