import React, { useState } from "react";
import { faLocationDot, faAngleDown } from "@fortawesome/free-solid-svg-icons";
import { useLocationContext } from "../../contexts/LocationContext";
import Card from "../Shared/Card/Card";
import Icon from "../Shared/Icon/Icon";
import SearchModal from "../Modal/SearchModal";

export default function Locationbar() {
  const [isOpen, setIsOpen] = useState(false);
  const { location } = useLocationContext();

  return (
    <>
      <Card
        styles="flex items-center justify-between w-full h-[3.75rem] px-6 cursor-pointer md:w-[23.625rem]"
        selected={isOpen && "selected"}
        onClick={() => setIsOpen(true)}
      >
        <Icon
          icon={faLocationDot}
          styles="text-xl md:text-[1.375rem] lg:text-[1.563rem]"
        />
        <div
          className={`text-lg font-semibold leading-normal ${
            isOpen ? "text-white" : "text-brand"
          } lg:text-xl`}
        >
          {location}
        </div>
        <Icon
          icon={faAngleDown}
          styles="text-xl md:text-2xl lg:text-[1.75rem]"
        />
      </Card>
      {isOpen && (
        <SearchModal
          onCloseModal={() => setIsOpen(false)}
          bgType="true"
          position="top"
          styles="px-5 w-full md:w-[40.625rem] md:px-0"
        />
      )}
    </>
  );
}
