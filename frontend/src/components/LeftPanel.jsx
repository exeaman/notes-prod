import FilterSelect from './FilterSelect';

export default function LeftPanel({
  searchParams,
  setSearchParams,
  openModal
}) {
  return (
    <div className="flex flex-col gap-8">
      <button
        onClick={() => openModal()}
        className="bg-[#681f2a] dark:bg-[#8e3d4c] text-[#f4efe6] text-sm font-bold uppercase tracking-widest py-4 px-6 hover:bg-[#4e1720] dark:hover:bg-[#a65363] transition-colors duration-300 cursor-pointer"
      >
        + New Entry
      </button>

      <div className="flex flex-col gap-5">
        <h2 className="text-[10px] font-bold tracking-[0.2em] uppercase text-[#766d65] dark:text-[#a99c91]">
          Archive Filters
        </h2>

        <FilterSelect
          value={searchParams.type}
          onChange={(type) =>
            setSearchParams({
              ...searchParams,
              type
            })
          }
        />

        {['TITLE', 'CONTENT', 'AUTHOR'].includes(searchParams.type) && (
          <input
            type="text"
            placeholder="Enter keyword..."
            className="w-full bg-transparent border-b border-[#d8d0c5] dark:border-[#3a3030] py-2 text-sm outline-none text-[#241f1c] dark:text-[#f1e9de] placeholder-[#a99c91] focus:border-[#681f2a] dark:focus:border-[#c77a86] transition-colors duration-300"
            value={searchParams.keyword}
            onChange={(e) =>
              setSearchParams({
                ...searchParams,
                keyword: e.target.value
              })
            }
          />
        )}

        {searchParams.type === 'DATE' && (
          <div className="flex gap-4">
            <input
              type="date"
              value={searchParams.start}
              className="w-1/2 bg-transparent border-b border-[#d8d0c5] dark:border-[#3a3030] text-xs py-2 outline-none text-[#241f1c] dark:text-[#f1e9de] cursor-pointer focus:border-[#681f2a] dark:focus:border-[#c77a86] transition-colors duration-300"
              onChange={(e) =>
                setSearchParams({
                  ...searchParams,
                  start: e.target.value
                })
              }
            />

            <input
              type="date"
              value={searchParams.end}
              className="w-1/2 bg-transparent border-b border-[#d8d0c5] dark:border-[#3a3030] text-xs py-2 outline-none text-[#241f1c] dark:text-[#f1e9de] cursor-pointer focus:border-[#681f2a] dark:focus:border-[#c77a86] transition-colors duration-300"
              onChange={(e) =>
                setSearchParams({
                  ...searchParams,
                  end: e.target.value
                })
              }
            />
          </div>
        )}
      </div>
    </div>
  );
}