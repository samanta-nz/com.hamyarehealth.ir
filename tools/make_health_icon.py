"""نشان اختصاصی «همیار سلامتی من» — قلب + نبض + برگ، روی گرادیان نعنایی/فیروزه‌ای.
کاملاً متفاوت با نشان «همیار من» (سرمه‌ای/کلاه فارغ‌التحصیلی/عدد ۹).
"""
from PIL import Image, ImageDraw
import math, os

SS = 4  # supersampling

BG_TOP = (207, 243, 230)     # نعنایی روشن
BG_BOTTOM = (79, 196, 178)   # فیروزه‌ای
HEART_TOP = (255, 122, 122)  # مرجانی روشن
HEART_BOTTOM = (233, 78, 112)  # سرخابی گرم

def heart_points(cx, cy, size, n=720):
    pts = []
    for i in range(n):
        t = 2 * math.pi * i / n
        x = 16 * math.sin(t) ** 3
        y = 13 * math.cos(t) - 5 * math.cos(2 * t) - 2 * math.cos(3 * t) - math.cos(4 * t)
        pts.append((cx + x * size / 17.0, cy - y * size / 17.0))
    return pts

def gradient(size, top, bottom):
    img = Image.new("RGB", (1, size))
    d = ImageDraw.Draw(img)
    for y in range(size):
        k = y / max(1, size - 1)
        d.point((0, y), fill=tuple(int(top[i] + (bottom[i] - top[i]) * k) for i in range(3)))
    return img.resize((size, size), Image.BILINEAR).convert("RGBA")

def rounded_mask(size, radius):
    m = Image.new("L", (size * SS, size * SS), 0)
    ImageDraw.Draw(m).rounded_rectangle([0, 0, size * SS - 1, size * SS - 1], radius=radius * SS, fill=255)
    return m.resize((size, size), Image.LANCZOS)

def circle_mask(size):
    m = Image.new("L", (size * SS, size * SS), 0)
    ImageDraw.Draw(m).ellipse([0, 0, size * SS - 1, size * SS - 1], fill=255)
    return m.resize((size, size), Image.LANCZOS)

def draw_art(size, heart_scale=0.60, with_bg=True, white_only=False, offset_y=0.0):
    """قلب + خط نبض + برگ. white_only برای آیکون monochrome."""
    s = size * SS
    art = Image.new("RGBA", (s, s), (0, 0, 0, 0))
    d = ImageDraw.Draw(art)
    cx, cy = s / 2, s / 2 + offset_y * s

    hw = s * heart_scale / 2.0
    pts = heart_points(cx, cy - hw * 0.05, hw * 1.55)

    if white_only:
        d.polygon(pts, fill=(255, 255, 255, 255))
        # نبض به‌صورت برش شفاف روی قلب سفید
        pulse = [
            (cx - hw * 0.95, cy - hw * 0.05),
            (cx - hw * 0.42, cy - hw * 0.05),
            (cx - hw * 0.20, cy - hw * 0.48),
            (cx + hw * 0.02, cy + hw * 0.42),
            (cx + hw * 0.26, cy - hw * 0.05),
            (cx + hw * 0.95, cy - hw * 0.05),
        ]
        d.line(pulse, fill=(0, 0, 0, 0), width=int(max(2, s * 0.030)), joint="curve")
        art = art.resize((size, size), Image.LANCZOS)
        return art

    # سایه‌ی نرم زیر قلب
    shadow = Image.new("RGBA", (s, s), (0, 0, 0, 0))
    ImageDraw.Draw(shadow).polygon(
        [(x, y + s * 0.018) for x, y in pts], fill=(28, 92, 84, 60))
    from PIL import ImageFilter
    shadow = shadow.filter(ImageFilter.GaussianBlur(s * 0.012))
    art.alpha_composite(shadow)

    # بدنه‌ی قلب با گرادیان
    grad = gradient(s, HEART_TOP, HEART_BOTTOM)
    heart_layer = Image.new("RGBA", (s, s), (0, 0, 0, 0))
    ImageDraw.Draw(heart_layer).polygon(pts, fill=(255, 255, 255, 255))
    art.paste(grad, (0, 0), heart_layer)

    # خط نبض سفید
    pulse = [
        (cx - hw * 0.95, cy - hw * 0.05),
        (cx - hw * 0.42, cy - hw * 0.05),
        (cx - hw * 0.20, cy - hw * 0.48),
        (cx + hw * 0.02, cy + hw * 0.42),
        (cx + hw * 0.26, cy - hw * 0.05),
        (cx + hw * 0.95, cy - hw * 0.05),
    ]
    d.line(pulse, fill=(255, 255, 255, 235), width=int(max(2, s * 0.026)), joint="curve")
    for p in (pulse[0], pulse[-1], pulse[2], pulse[3]):
        r = s * 0.013
        d.ellipse([p[0] - r, p[1] - r, p[0] + r, p[1] + r], fill=(255, 255, 255, 235))

    # برگ کوچک بالای قلب (نماد تندرستی)
    lx, ly = cx + hw * 0.72, cy - hw * 0.96
    leaf = Image.new("RGBA", (s, s), (0, 0, 0, 0))
    ld = ImageDraw.Draw(leaf)
    lw, lh = s * 0.115, s * 0.062
    ld.ellipse([lx - lw, ly - lh, lx + lw, ly + lh], fill=(76, 187, 132, 255))
    leaf = leaf.rotate(-32, resample=Image.BICUBIC, center=(lx, ly))
    art.alpha_composite(leaf)
    ld2 = ImageDraw.Draw(art)
    ld2.line([(lx - lw * 0.7, ly + lh * 0.35), (lx + lw * 0.8, ly - lh * 0.35)],
             fill=(255, 255, 255, 150), width=int(max(1, s * 0.008)))

    return art.resize((size, size), Image.LANCZOS)

def compose(size, mask_fn, art_scale, offset_y=0.0, radius_pct=0.20):
    bg = gradient(size, BG_TOP, BG_BOTTOM)
    art = draw_art(size, heart_scale=art_scale, offset_y=offset_y)
    out = bg.copy()
    out.alpha_composite(art)
    mask = mask_fn(size)
    out.putalpha(mask)
    return out

def main():
    res = os.environ.get("RES_DIR", "apps/hamyar-health/src/main/res")
    os.makedirs(f"{res}/drawable-nodpi", exist_ok=True)
    for density, size in (("mdpi", 48), ("hdpi", 72), ("xhdpi", 96), ("xxhdpi", 144), ("xxxhdpi", 192)):
        # square (legacy)
        compose(size, lambda s: rounded_mask(s, int(s * 0.22)), 0.54, offset_y=-0.045).save(f"{res}/mipmap-{density}/ic_launcher.png")
        # round
        compose(size, circle_mask, 0.48, offset_y=-0.045).save(f"{res}/mipmap-{density}/ic_launcher_round.png")
        for f in ("ic_launcher_boy.png", "ic_launcher_girl.png"):
            p = f"{res}/mipmap-{density}/{f}"
            if os.path.exists(p):
                os.remove(p)
    # adaptive foreground (سهم امن: ۶۶٪ مرکز)
    fg = draw_art(432, heart_scale=0.40, offset_y=-0.02)
    fg.save(f"{res}/drawable-nodpi/ic_launcher_foreground.png")
    mono = draw_art(432, heart_scale=0.40, white_only=True, offset_y=-0.02)
    mono.save(f"{res}/drawable-nodpi/ic_launcher_monochrome.png")
    print("icons written")

main()
