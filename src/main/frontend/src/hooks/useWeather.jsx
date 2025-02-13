import { useQueries } from "@tanstack/react-query";
import { getApi } from "../api/api";
import { useLocationContext } from "../contexts/LocationContext";

export default function useWeather(apis, params, errorOption) {
  const { location } = useLocationContext();

  const queryResults = useQueries({
    queries: apis.map((api) => {
      if (api) {
        return {
          queryKey: [api, location],
          queryFn: () => getApi(api, params),
          staleTime: 1000 * 60 * 5,
          useErrorBoundary: errorOption,
        };
      }
    }),
  });

  console.log("=======> useWeather.jsx");

  return queryResults;
}
