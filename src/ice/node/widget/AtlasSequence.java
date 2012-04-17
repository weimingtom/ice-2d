package ice.node.widget;

import ice.graphic.texture.Texture;
import ice.node.OverlayParent;

import java.util.ArrayList;
import java.util.List;

public class AtlasSequence extends OverlayParent<AtlasOverlay> {

    public AtlasSequence(int eachWidth, int eachHeight, float margin) {
        this.eachWidth = eachWidth;
        this.eachHeight = eachHeight;
        this.margin = margin;

        setPrePrepareLength(3);
    }

    public void setPrePrepareLength(int prePrepareLength) {
        List<AtlasOverlay> prePrepare = new ArrayList<AtlasOverlay>(prePrepareLength);

        for (int i = 0; i < prePrepareLength; i++) {
            AtlasOverlay atlasOverlay = new AtlasOverlay(eachWidth, eachHeight);
            prePrepare.add(atlasOverlay);
            atlasOverlay.setVisible(false);
        }

        addChildren(prePrepare);
    }

    public void setAtlas(Texture atlasTexture, int splitU, int splitV) {
        this.atlasTexture = atlasTexture;
        this.splitU = splitU;
        this.splitV = splitV;
    }

    public void setSequence(int[] sequence) {
        int size = size();

        float totalWidth = sequence.length * eachWidth + (sequence.length - 1) * margin;

        float mostRight = totalWidth / 2 - eachWidth / 2;

        if (size < sequence.length || size == 0) {
            for (int i = 0; i < size; i++) {
                AtlasOverlay atlasOverlay = get(i);
                atlasOverlay.setSplitU_V(splitU, splitV);
                atlasOverlay.setAtlasIndex(sequence[i], atlasTexture);
                atlasOverlay.setPos(mostRight - i * (eachWidth + margin), 0);
                atlasOverlay.setVisible(true);
            }

            List<AtlasOverlay> more = new ArrayList<AtlasOverlay>(sequence.length);

            for (int i = 0; i < sequence.length - size; i++) {
                AtlasOverlay atlasOverlay = new AtlasOverlay(eachWidth, eachHeight, splitU, splitV);
                atlasOverlay.setAtlasIndex(sequence[i], atlasTexture);
                atlasOverlay.setPos(mostRight - i * (eachWidth / 2 + margin), 0);

                more.add(atlasOverlay);
            }

            addChildren(more);
        }
        else {
            for (int i = 0; i < size; i++) {

                AtlasOverlay atlasOverlay = get(i);

                if (i < sequence.length) {
                    atlasOverlay.setPos(mostRight - i * (eachWidth / 2 + margin), 0);
                    atlasOverlay.setSplitU_V(splitU, splitV);
                    atlasOverlay.setAtlasIndex(sequence[i], atlasTexture);
                    atlasOverlay.setVisible(true);
                }
                else {
                    atlasOverlay.setVisible(false);
                }

            }
        }

    }

    private Texture atlasTexture;
    private int splitU, splitV;

    private int eachWidth, eachHeight;
    private float margin;
}
