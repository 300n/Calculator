import java.util.ArrayList; 


public class ThemeManager {
    private ArrayList<Theme> themes = new ArrayList<>();
    private Theme currentTheme;

    public ThemeManager(){
        try{
            this.initializeThemes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeThemes() {
        themes.add(new Theme("Basique",
            "https://imgs.search.brave.com/9eMLyTsdf8TDRA3bhIObAQRZyKieeJ-4-s9Rz7I8Iqs/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pbWcu/ZnJlZXBpay5jb20v/cGhvdG9zLWdyYXR1/aXRlL2NvbmNlcHRp/b24tb21icmUtbHVt/aWVyZS1kdS1tdXIt/Y2ltZW50XzUzODc2/LTk1MzIwLmpwZz9z/ZW10PWFpc19oeWJy/aWQmdz03NDAmcT04/MA",
            "rgba(55, 228, 255, 1)", "rgba(0, 179, 255, 0.8)"));
        themes.add(new Theme("Hello Kitty",
            "https://imgs.search.brave.com/LCcnVeC4KVS2MOuab6sUffMQ7SxIpTmrjS-YrJk7O8U/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzLzJhLzg3/Lzg3LzJhODc4Nzhh/OTRiNmU3NDFhM2Mx/NzdiMzJjNGRiZGNh/LmpwZw",
            "rgba(255,255,255,0.8)", "rgba(255, 114, 229, 0.8)"));
        themes.add(new Theme("Shrek", 
            "https://imgs.search.brave.com/4OnMmdlRFol-XfiIE42DmJ5-8Z6OR9kIXFa6iUTmqCM/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJzLmNvbS9p/bWFnZXMvaGQvc2hy/ZWstcy1oZWFkLWNv/bGxhZ2UtMXFobWln/bzNjNHMzem41eS5q/cGc",
            "rgba(255,255,255,0.8)", "rgba(0, 255, 0, 0.8)"));
        themes.add(new Theme("Cars", 
            "https://imgs.search.brave.com/Rc4HyNjUVENquntwryAxPKGmiP8gVExb555AglwhjB8/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzLzFkLzNk/LzMxLzFkM2QzMTIy/NDNiMzM4ODQ3OGY2/OWYwZTMwM2NmNTQ3/LmpwZw",
            "rgba(255,255,255,0.8)", "rgba(255, 0, 0, 0.8)"));
        themes.add(new Theme("Pat Patrouille", 
            "https://images.shcdn.de/resized/w680/p/baumwolljersey-paw-patrol-hellblau_127.212-0801_2.jpg",
            "rgba(255, 255, 255, 0.8)", "rgba(255, 233, 31, 1)"));
        themes.add(new Theme("Spider Man", 
            "https://lesciseauxmagiques.fr/52405-large_default/tissu-en-coton-spiderman.jpg",
            "rgba(255, 255, 255, 0.8)", "rgba(255, 0, 0, 0.8)"));
        currentTheme = themes.get(0);
    }

    public Theme getCurrentTheme(){
        return this.currentTheme;
    }

    public ArrayList<Theme> getThemes(){
        return this.themes;
    }
    public void setCurrentTheme(Theme theme){
        this.currentTheme = theme;
    }
}