package si.fri.rgti;

public class Mushroom
        extends GameObject
        implements Interactable {

    /* TODO
    members: color, poison bool, clipping box?
    methods: inherited
     */
    public double color;
    public boolean poison;


    public Mushroom() {
        // TODO
    }

    @Override
    public void Interact(Player player) {
        player.pobraneGobe++;

        if(poison) {
            player.HP -= 5;
        }

    }



}
