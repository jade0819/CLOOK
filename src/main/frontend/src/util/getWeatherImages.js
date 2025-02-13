import noCloudAfternoonBackground from "../assets/imgs/background/noCloudAfternoonBackground.jpg";
import noCloudNightBackground from "../assets/imgs/background/noCloudNightBackground.jpg";
import cloudAfternoonBackground from "../assets/imgs/background/cloudAfternoonBackground.jpg";
import cloudNightBackground from "../assets/imgs/background/cloudNightBackground.jpg";
import snowCloudyBackground from "../assets/imgs/background/snowCloudyBackground.jpg";
import rainCloudyBackground from "../assets/imgs/background/rainCloudyBackground.jpg";
import grayBackground from "../assets/imgs/background/grayBackground.jpg";

import sun from "../assets/imgs/icon/sun.png";
import cloud from "../assets/imgs/icon/cloud.png";
import moon from "../assets/imgs/icon/moon.png";
import moonCloud from "../assets/imgs/icon/moonCloud.png";
import rain from "../assets/imgs/icon/rain.png";
import sleet from "../assets/imgs/icon/sleet.png";
import snow from "../assets/imgs/icon/snow.png";
import sunCloud from "../assets/imgs/icon/sunCloud.png";

import characterA from "../assets/imgs/character/characterA.png";
import characterB from "../assets/imgs/character/characterB.png";
import characterCold from "../assets/imgs/character/characterCold.png";
import characterHot from "../assets/imgs/character/characterHot.png";
import characterRain from "../assets/imgs/character/characterRain.png";
import characterSnow from "../assets/imgs/character/characterSnow.png";

import pm from "../assets/imgs/todaysCard/pm.png";
import uv from "../assets/imgs/todaysCard/uv.png";
import wind from "../assets/imgs/todaysCard/wind.png";
import humidity from "../assets/imgs/todaysCard/humidity.png";
import umbrella from "../assets/imgs/todaysCard/umbrella.png";
import suns from "../assets/imgs/todaysCard/sun.png";

export function getWeatherImages(type, value) {
  if (!type || (!value && value != 0)) return;

  let image = null;

  if (type === "bg") {
    switch (value) {
      // background
      case "구름없는낮":
        image = noCloudAfternoonBackground;
        break;
      case "구름없는밤":
        image = noCloudNightBackground;
        break;
      case "구름많은낮":
        image = cloudAfternoonBackground;
        break;
      case "구름많은밤":
        image = cloudNightBackground;
        break;
      case "눈오는흐린배경":
        image = snowCloudyBackground;
        break;
      case "비오는흐린배경":
        image = rainCloudyBackground;
        break;
      case "흐린회색배경":
        image = grayBackground;
        break;
      default:
        image = grayBackground;
        break;
    }
  } else if (type === "character") {
    // character
    switch (value) {
      // background
      case "0":
      case 0:
        image = characterA; // 기본A타입
        break;
      case "1":
      case 1:
        image = characterB; // 기본B타입
        break;
      case "더움":
        image = characterHot; // 더워하는
        break;
      case "추움":
        image = characterCold; // 추워하는
        break;
      case "비":
        image = characterRain; // 비 오는
        break;
      case "눈":
        image = characterSnow; // 눈 오는
        break;
      default:
        image = characterA;
        break;
    }
  } else if (type === "icon") {
    // icon
    switch (value) {
      case "해":
        image = sun;
        break;
      case "비":
        image = rain;
        break;
      case "눈":
        image = snow;
        break;
      case "진눈깨비":
        image = sleet;
        break;
      case "달":
        image = moon;
        break;
      case "구름":
        image = cloud;
        break;
      case "해구름":
        image = sunCloud;
        break;
      case "달구름":
        image = moonCloud;
        break;
      default:
        image = "";
        break;
    }
  } else if (type === "cardIcon") {
    switch (value) {
      case "미세먼지":
        image = pm;
        break;
      case "자외선":
        image = uv;
        break;
      case "바람":
        image = wind;
        break;
      case "습도":
        image = humidity;
        break;
      case "강수량":
        image = umbrella;
        break;
      case "일출일몰":
        image = suns;
        break;
      default:
        break;
    }
  }

  return image;
}
